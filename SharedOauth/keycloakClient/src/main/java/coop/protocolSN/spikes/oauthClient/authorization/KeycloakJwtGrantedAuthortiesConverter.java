package coop.protocolSN.spikes.oauthClient.authorization;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.log.LogMessage;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class KeycloakJwtGrantedAuthortiesConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String authority : getAuthorities(source)) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + authority));
        }
        return new JwtAuthenticationToken(source, grantedAuthorities);
    }

    private Collection<String> getAuthorities(Jwt jwt) {
        String claimName = "realm_access";
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(LogMessage.format("Looking for scopes in claim %s", claimName));
        }
        Map<String, List<String>> authorities = jwt.getClaim(claimName);

        return authorities.get("roles");
    }
}
