package coop.stlma.tech.protocolsn.spike;

import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.event.ApplicationStartupEvent;
import jakarta.inject.Singleton;

@Singleton
public class StartupListener implements ApplicationEventListener<ApplicationStartupEvent> {

//    private final SpikeTwoClient client;
//
//    public StartupListener(SpikeTwoClient client) {
//        this.client = client;
//    }

    private final HttpClient client;

    public StartupListener(@Client("spike-client-two") HttpClient client) {
        this.client = client;
    }

    @Override
    public void onApplicationEvent(ApplicationStartupEvent event) {
        System.out.println("onApplicationEvent: " + event);
//        System.out.println(client.getUnsecured());
//        System.out.println(client.getSecuredWithNoRole());
//        System.out.println(client.getSecured());
//        try {
//            System.out.println(client.getSecuredWithInvalidRole());
//        } catch (HttpClientResponseException e) {
//            System.out.println(e.getStatus().getCode() + " == 403");
//        }
        System.out.println(client.toBlocking().exchange("/unsecured").getBody(String.class).get());
        System.out.println(client.toBlocking().exchange("/secured/validRole").getBody(String.class).get());
        System.out.println(client.toBlocking().exchange("/secured/noRole").getBody(String.class).get());
        try {
            System.out.println(client.toBlocking().exchange("/secured/wrongRole"));
        } catch (HttpClientResponseException e) {
            System.out.println(e.getStatus().getCode() + " == 403");
        }

    }
}
