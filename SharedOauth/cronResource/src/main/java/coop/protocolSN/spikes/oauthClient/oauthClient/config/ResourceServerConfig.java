package coop.protocolSN.spikes.oauthClient.oauthClient.config;

import coop.protocolSN.spikes.oauthClient.oauthClient.authorization.KeycloakJwtGrantedAuthortiesConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class ResourceServerConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
					authorizationManagerRequestMatcherRegistry.requestMatchers("/cron/**")
							.hasAuthority("ROLE_cron"))
				.oauth2ResourceServer(configurer ->
						configurer.jwt(jwtConfigurer ->
								jwtConfigurer.jwtAuthenticationConverter(new KeycloakJwtGrantedAuthortiesConverter())));
		return http.build();
	}

}
