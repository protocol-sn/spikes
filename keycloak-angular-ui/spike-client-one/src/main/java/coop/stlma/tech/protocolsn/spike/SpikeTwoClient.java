package coop.stlma.tech.protocolsn.spike;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import org.reactivestreams.Publisher;

@Client("spike-client-two")
public interface SpikeTwoClient extends SpikeTwoOperations {
    @Get("/unsecured")
    String getUnsecured();

    @Get("/secured/validRole")
    String getSecured();

    @Get("/secured/wrongRole")
    String getSecuredWithInvalidRole();

    @Get("/secured/noRole")
    String getSecuredWithNoRole();
}
