package coop.protocolSN.spikes.oauthClient.config;

import coop.protocolSN.spikes.oauthClient.authorization.KeycloakJwtGrantedAuthortiesConverter;
import coop.protocolSN.spikes.oauthClient.authorization.KeycloakLogoutHandler;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

	private final KeycloakLogoutHandler keycloakLogoutHandler;

	public SecurityConfig(KeycloakLogoutHandler keycloakLogoutHandler) {
		this.keycloakLogoutHandler = keycloakLogoutHandler;
	}

	@Bean
	protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
		return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				.authorizeHttpRequests(authorize ->
						authorize
								.requestMatchers("/customers")
								.hasAuthority("ROLE_user")
								.requestMatchers("/direct/customers")
								.hasAuthority("ROLE_user")
								.anyRequest()
								.permitAll()
				)
				.oauth2Login(Customizer.withDefaults())
				.oauth2Client(Customizer.withDefaults())
				.oauth2ResourceServer(configurer ->
						configurer.jwt(jwtConfigurer ->
								jwtConfigurer.jwtAuthenticationConverter(new KeycloakJwtGrantedAuthortiesConverter())))
				.logout(logout ->
						logout.addLogoutHandler(keycloakLogoutHandler)
								.logoutSuccessUrl("/"));

		return httpSecurity.build();
	}

	@Bean
	public GrantedAuthoritiesMapper userAuthoritiesMapperForKeycloak() {
		return authorities -> {
			Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
			var authority = authorities.iterator().next();
			boolean isOidc = authority instanceof OidcUserAuthority;

			if (isOidc) {
				var oidcUserAuthority = (OidcUserAuthority) authority;
				var userInfo = oidcUserAuthority.getUserInfo();

				if (userInfo.hasClaim("realm_access")) {
					var realmAccess = userInfo.getClaimAsMap("realm_access");
					var roles = (Collection<String>) realmAccess.get("roles");
					mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
				}
			} else {
				var oauth2UserAuthority = (OAuth2UserAuthority) authority;
				Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();

				if (userAttributes.containsKey("realm_access")) {
					var realmAccess =  (Map<String,Object>) userAttributes.get("realm_access");
					var roles =  (Collection<String>) realmAccess.get("roles");
					mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
				}
			}

			return mappedAuthorities;
		};
	}

	@Bean
	public JwtDecoder jwtDecoder(OAuth2ResourceServerProperties properties) {
		NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(
				properties.getJwt().getJwkSetUri()).build();

		return jwtDecoder;
	}

	Collection<GrantedAuthority> generateAuthoritiesFromClaim(Collection<String> roles) {
		return roles.stream()
				.map(role -> new SimpleGrantedAuthority("ROLE_" + role))
				.collect(Collectors.toList());
	}
}
