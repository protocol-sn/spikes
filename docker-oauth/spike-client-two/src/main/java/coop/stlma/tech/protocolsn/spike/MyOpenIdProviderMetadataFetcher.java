package coop.stlma.tech.protocolsn.spike;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.exceptions.DisabledBeanException;
import io.micronaut.core.annotation.Blocking;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.optim.StaticOptimizations;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientException;
import io.micronaut.security.oauth2.client.DefaultOpenIdProviderMetadata;
import io.micronaut.security.oauth2.client.DefaultOpenIdProviderMetadataFetcher;
import io.micronaut.security.oauth2.client.OpenIdProviderMetadataFetcher;
import io.micronaut.security.oauth2.configuration.OpenIdClientConfiguration;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@Singleton
@Replaces(DefaultOpenIdProviderMetadataFetcher.class)
public class MyOpenIdProviderMetadataFetcher implements OpenIdProviderMetadataFetcher {
    public static final DefaultOpenIdProviderMetadataFetcher.Optimizations OPTIMIZATIONS = StaticOptimizations.get(DefaultOpenIdProviderMetadataFetcher.Optimizations.class).orElse(new DefaultOpenIdProviderMetadataFetcher.Optimizations(Collections.emptyMap()));

    private static final Logger LOG = LoggerFactory.getLogger(MyOpenIdProviderMetadataFetcher.class);
    private final HttpClient client;
    private final OpenIdClientConfiguration openIdClientConfiguration;

    /**
     * @param openIdClientConfiguration OpenID Client Configuration
     * @param client HTTP Client
     */
    public MyOpenIdProviderMetadataFetcher(OpenIdClientConfiguration openIdClientConfiguration,
                                                @Client HttpClient client) {
        this.openIdClientConfiguration = openIdClientConfiguration;
        this.client = client;
    }

    @Override
    @NonNull
    public String getName() {
        return openIdClientConfiguration.getName();
    }

    @Override
    @Blocking
    @NonNull
    public DefaultOpenIdProviderMetadata fetch() {
        return OPTIMIZATIONS.findMetadata(openIdClientConfiguration.getName())
                .map(Supplier::get)
                .orElseGet(fetch(openIdClientConfiguration));
    }

    @NonNull
    private Supplier<DefaultOpenIdProviderMetadata> fetch(@NonNull OpenIdClientConfiguration openIdClientConfiguration) {
        return () -> openIdClientConfiguration.getIssuer()
                .map(this::fetch)
                .map(metadata -> {
                    metadata.setName(openIdClientConfiguration.getName());
                    return metadata;
                })
                .orElse(new DefaultOpenIdProviderMetadata(openIdClientConfiguration.getName()));
    }

    @NonNull
    private DefaultOpenIdProviderMetadata fetch(@NonNull URL issuer) {
        try {
            LOG.debug("------------------------------------------------------");
            LOG.debug("Issuer URL is {}", issuer);
            URL configurationUrl = new URL(issuer, StringUtils.prependUri(issuer.getPath(), openIdClientConfiguration.getConfigurationPath()));
            if (LOG.isDebugEnabled()) {
                LOG.debug("Sending request for OpenID configuration for provider [{}] to URL [{}] running in thread {}", openIdClientConfiguration.getName(), configurationUrl, Thread.currentThread().getName());
            }
            LOG.debug("Configuration URL is {} with toString of {}", configurationUrl, configurationUrl.toString());
            LOG.debug("------------------------------------------------------");
            return client.toBlocking().retrieve(configurationUrl.toString(), DefaultOpenIdProviderMetadata.class);
        } catch (HttpClientException e) {
            throw new DisabledBeanException("Bean of type " + DefaultOpenIdProviderMetadata.class.getName() + " with name quailfier " + openIdClientConfiguration.getName() + " is disabled. Failed to retrieve OpenID configuration for " + openIdClientConfiguration.getName());
        } catch (MalformedURLException e) {
            throw new DisabledBeanException("Bean of type " + DefaultOpenIdProviderMetadata.class.getName() + " with name quailfier " + openIdClientConfiguration.getName() + " is disabled. Failure parsing issuer URL " + issuer);
        }
    }

    /**
     * AOT Optimizations.
     */
    public static class Optimizations {
        private final Map<String, Supplier<DefaultOpenIdProviderMetadata>> suppliers;

        /**
         * @param suppliers Map with key being the OpenID Name qualifier and
         */
        public Optimizations(Map<String, Supplier<DefaultOpenIdProviderMetadata>> suppliers) {
            this.suppliers = suppliers;
        }

        /**
         * @param name name qualifier
         * @return {@link DefaultOpenIdProviderMetadata} supplier or empty optional if not found for the given name qualifier.
         */
        public Optional<Supplier<DefaultOpenIdProviderMetadata>> findMetadata(String name) {
            return Optional.ofNullable(suppliers.get(name));
        }
    }
}
