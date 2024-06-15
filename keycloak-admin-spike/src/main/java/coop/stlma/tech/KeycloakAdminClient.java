package coop.stlma.tech;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.annotation.Client;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Client("keycloak-admin")
public interface KeycloakAdminClient {

    @Get("/admin/realms/{realm}/users")
    Flux<String> getUsers(@PathVariable("realm") String realm);

    @Post(value = "/admin/realms/{realm}/users", consumes = MediaType.APPLICATION_JSON)
    Mono<Void> createUser(@PathVariable("realm") String realm, @Body UserRepresentation user);

    @Put(value = "/admin/realms/{realm}/users", consumes = MediaType.APPLICATION_JSON)
    Mono<Void> updateUser(@PathVariable("realm") String realm, @Body UserRepresentation user);
}
