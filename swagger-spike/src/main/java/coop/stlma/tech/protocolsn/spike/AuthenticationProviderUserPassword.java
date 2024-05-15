package coop.stlma.tech.protocolsn.spike;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationFailureReason;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.provider.HttpRequestAuthenticationProvider;
import jakarta.inject.Singleton;

@Singleton
public class AuthenticationProviderUserPassword<String> implements HttpRequestAuthenticationProvider<String> {
    @Override
    public @NonNull AuthenticationResponse authenticate(@Nullable HttpRequest<String> requestContext,
                                                        @NonNull AuthenticationRequest<java.lang.String, java.lang.String> authenticationRequest) {
        return authenticationRequest.getIdentity().equals("sherlock") && authenticationRequest.getSecret().equals("password")
                ? AuthenticationResponse.success(authenticationRequest.getIdentity())
                : AuthenticationResponse.failure(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH);
    }
}
