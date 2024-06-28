package coop.stlma.tech.protocolsn.spike;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.View;

import java.util.HashMap;
import java.util.Map;

@Controller
@Secured(SecurityRule.IS_ANONYMOUS)
public class ViewController {

    private final HttpClient spikeTwoClient;

    public ViewController(@Client("spike-client-two") HttpClient spikeTwoClient) {
        this.spikeTwoClient = spikeTwoClient;
    }

    @Produces(MediaType.TEXT_HTML)
    @View("user")
    @Get
    @Secured(SecurityRule.IS_ANONYMOUS)
    public HttpResponse<Map<String, Object>> homePage() {
        return HttpResponse.ok(new HashMap<>());
    }

    @Get("/trigger")
    @Secured(SecurityRule.IS_ANONYMOUS)
    @ExecuteOn(TaskExecutors.BLOCKING)
    public HttpResponse<Map<String, Object>> trigger() {

        String unsecuredResponse = spikeTwoClient.toBlocking().exchange("/unsecured").getBody(String.class).get();
        System.out.println(unsecuredResponse);
        String validRoleResponse = spikeTwoClient.toBlocking().exchange("/secured/validRole").getBody(String.class).get();
        System.out.println(validRoleResponse);
        String noRoleResponse = spikeTwoClient.toBlocking().exchange("/secured/noRole").getBody(String.class).get();
        System.out.println(noRoleResponse);
        String wrongRoleVariable = "";
        try {
            wrongRoleVariable = spikeTwoClient.toBlocking().exchange("/secured/wrongRole").getBody(String.class).get();
            System.out.println(wrongRoleVariable);
        } catch (HttpClientResponseException e) {
            wrongRoleVariable = String.valueOf(e.getStatus().getCode());
            System.out.println(wrongRoleVariable + " == 403");
        }

        return HttpResponse.ok(Map.of(
                "/unsecured", unsecuredResponse,
                "/secured/validRole", validRoleResponse,
                "/secured/noRole", noRoleResponse,
                "/secured/wrongRole", wrongRoleVariable));
    }
}
