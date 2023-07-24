package coop.protocolSN.spikes.oauthClient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

	@Bean
	public WebClient clientFlowWebClient(
			OAuth2AuthorizedClientManager authorizedClientServiceReactiveOAuth2AuthorizedClientManager) {
		ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Client =
				new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientServiceReactiveOAuth2AuthorizedClientManager);
		oauth2Client.setDefaultClientRegistrationId("keycloak-client-credentials");
		return WebClient.builder()
				.filter(oauth2Client)
				.build();
	}

	@Bean
	public OAuth2AuthorizedClientManager authorizedClientManager(
			ClientRegistrationRepository clientRegistrationRepository,
			OAuth2AuthorizedClientRepository authorizedClientRepository,
			OAuth2AuthorizedClientService clientService) {

		OAuth2AuthorizedClientProvider authorizedClientProvider =
				OAuth2AuthorizedClientProviderBuilder.builder()
						.authorizationCode()
						.refreshToken()
						.clientCredentials()
						.build();

		AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
				new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, clientService);
		authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

		return authorizedClientManager;
	}

}
