package coop.stlma.tech.protocolsn.spike;

import io.micronaut.http.client.annotation.Client;

@Client(id="keycloak", path="/realms/")
public interface KeycloakClient {
}
