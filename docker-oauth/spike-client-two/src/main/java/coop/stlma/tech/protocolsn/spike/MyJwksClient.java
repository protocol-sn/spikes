package coop.stlma.tech.protocolsn.spike;

import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.core.util.StringUtils;
import io.micronaut.core.util.SupplierUtil;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.HttpClientConfiguration;
import io.micronaut.http.client.HttpClientRegistry;
import io.micronaut.http.client.HttpVersionSelection;
import io.micronaut.http.client.LoadBalancer;
import io.micronaut.http.client.ServiceHttpClientConfiguration;
import io.micronaut.http.client.exceptions.HttpClientException;
import io.micronaut.inject.qualifiers.Qualifiers;
import io.micronaut.security.token.jwt.config.JwtConfigurationProperties;
import io.micronaut.security.token.jwt.signature.jwks.HttpClientJwksClient;
import io.micronaut.security.token.jwt.signature.jwks.JwksClient;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Singleton
@Replaces(HttpClientJwksClient.class)
@Requires(classes = HttpClient.class)
@Requires(property = JwtConfigurationProperties.PREFIX + ".signatures.jwks-client.http-client.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
public class MyJwksClient  implements JwksClient {

    private static final Logger LOG = LoggerFactory.getLogger(MyJwksClient.class);

    private final BeanContext beanContext;
    private final HttpClientRegistry<HttpClient> clientRegistry;
    private final Supplier<HttpClient> defaultJwkSetClient;
    private final ConcurrentHashMap<String, HttpClient> jwkSetClients = new ConcurrentHashMap<>();

    /**
     *
     * @param beanContext BeanContext
     * @param clientRegistry HTTP Client Registry
     * @param defaultClientConfiguration Default HTTP Client Configuration
     */
    public MyJwksClient(BeanContext beanContext, HttpClientRegistry<HttpClient> clientRegistry, HttpClientConfiguration defaultClientConfiguration) {
        this.beanContext = beanContext;
        this.clientRegistry = clientRegistry;
        this.defaultJwkSetClient = SupplierUtil.memoized(() -> beanContext.createBean(HttpClient.class, LoadBalancer.empty(), defaultClientConfiguration));
    }

    @Override
    @SingleResult
    public Publisher<String> load(@Nullable String providerName, @NonNull String url) throws HttpClientException {
        LOG.debug("Loading JWK from {} for provider {}", url, providerName);
        return Mono.from(getClient(providerName)
                        .retrieve(url))
                .onErrorResume(HttpClientException.class, throwable -> {
                    if (LOG.isErrorEnabled()) {
                        LOG.error("Exception loading JWK from " + url, throwable);
                    }
                    return Mono.empty();
                });
    }

    /**
     * Retrieves an HTTP client for the given provider.
     *
     * @param providerName The provider name
     * @return An HTTP client to use to send the JWKS request
     */
    protected HttpClient getClient(@Nullable String providerName) {
        if (providerName == null) {
            return defaultJwkSetClient.get();
        }
        return jwkSetClients.computeIfAbsent(providerName, provider ->
                beanContext.findBean(ServiceHttpClientConfiguration.class, Qualifiers.byName(provider))
                        .map(serviceConfig -> this.clientRegistry.getClient(HttpVersionSelection.forClientConfiguration(serviceConfig), provider, "/"))
                        .orElseGet(defaultJwkSetClient));
    }
}
