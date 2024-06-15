package coop.stlma.tech;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.serde.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Controller("/keycloak-alert")
@Slf4j
@Secured(SecurityRule.IS_ANONYMOUS)
public class KeycloakAlertController {

    private UserRepresentation alertUser;

    private final ObjectMapper objectMapper;

    public KeycloakAlertController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Post
    public HttpResponse<UserRepresentation> receiveAlert(@Body String userString) throws IOException {
        log.info("raw body: {}", userString);
        UserRepresentation user = objectMapper.readValue(userString, UserRepresentation.class);
        log.info("Received alert from user {}", user.getUsername());
        alertUser = user;
        return HttpResponse.created(user);
    }

    @Get
    public HttpResponse<UserRepresentation> getAlertUser() {
        return HttpResponse.ok(alertUser);
    }

}
