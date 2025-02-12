package coop.stlma.tech.protocolsn.spike;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.annotation.Secured;

@Controller("/secured")
public class WorkaroundController {

    private final HttpClient client;

    public WorkaroundController(@Client("spike-client-two") HttpClient client) {
        this.client = client;
    }

    @Get("/workaround")
    @Secured("spike-two")
    public String doWorkaround() {
        return "Got '" + client.toBlocking().exchange("/secured/spike").getBody(String.class).get() + "' from other resource server";
    }
}
