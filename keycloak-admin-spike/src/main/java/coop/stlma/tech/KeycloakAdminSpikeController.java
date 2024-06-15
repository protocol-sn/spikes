package coop.stlma.tech;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@Secured(SecurityRule.IS_ANONYMOUS)
public class KeycloakAdminSpikeController {

    private final KeycloakAdminClient keycloakAdminClient;

    public KeycloakAdminSpikeController(KeycloakAdminClient keycloakAdminClient) {
        this.keycloakAdminClient = keycloakAdminClient;
    }

    @Post(value = "/create-user", produces = MediaType.TEXT_PLAIN, consumes = MediaType.APPLICATION_JSON)
    public Mono<String> createUser(@Body UserRepresentation user) {
        return keycloakAdminClient.createUser("master", user)
//                .then(Mono.fromCallable(() -> {
//                    user.setEnabled(true);
//                    keycloakAdminClient.updateUser("master", user)
//                }));
                .then(Mono.fromCallable(() -> "User created"));
    }

    @Get(value = "/list-users", produces = MediaType.APPLICATION_JSON)
    public Flux<String> listUsers() {
        return keycloakAdminClient.getUsers("master");
    }

    @Get(uri="/", produces="text/plain")
    public String index() {
        return "Example Response";
    }
}