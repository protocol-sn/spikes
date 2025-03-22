package coop.stlma.tech.protocolsn.spike;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.RequestAttribute;
import io.micronaut.http.client.HttpClient;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.security.utils.SecurityService;
import reactor.core.publisher.Mono;

import java.util.Map;

@Controller
public class UserController {

    public static final @NonNull Argument<Map<String, Object>> KC_BODY_TYPE = Argument.mapOf(String.class, Object.class);
    private final HttpClient keycloakClient;
    private final SecurityService securityService;

    public UserController(HttpClient keycloakClient,
                          SecurityService securityService) {
        this.keycloakClient = keycloakClient;
        this.securityService = securityService;
    }

    @Get("/user")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public Mono<HttpResponse<MyUserInfo>> getUserInfo(@RequestAttribute("micronaut.TOKEN") String token) {
        HttpRequest<?> request = HttpRequest.GET("http://localhost:9080/realms/spike-realm/protocol/openid-connect/userinfo")
                .bearerAuth(token.substring("Bearer ".length()));
        return Mono.from(keycloakClient.exchange(request, KC_BODY_TYPE))
                .map(response -> {
                    if (response.getBody(KC_BODY_TYPE).isEmpty()) {
                        throw new RuntimeException("Oh no!");
                    }
                    Map<String, Object> responseBody = response.getBody(KC_BODY_TYPE).get();
                    MyUserInfo returnMe = new MyUserInfo();
                    returnMe.setSub((String) responseBody.get("sub"));
                    returnMe.setEmailVerified((Boolean) responseBody.get("email_verified"));
                    returnMe.setPreferredUsername((String) responseBody.get("preferred_username"));
                    return HttpResponse.ok(returnMe);
                });
    }
}
