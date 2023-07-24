package coop.protocolSN.spikes.oauthClient.authorization;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableScheduling
public class ScheduleConfig {

    private final Log logger = LogFactory.getLog(ScheduleConfig.class);
    private final WebClient webClient;
    private final String cronBaseUri;

    public ScheduleConfig(WebClient webClient,
                          @Value("${cron.base-uri}") String cronBaseUri
    ) {
        this.webClient = webClient;
        this.cronBaseUri = cronBaseUri;
    }

    @Scheduled(fixedDelay = 10000)
    public void doCronThing() {
        String[] messages = this.webClient
                .get()
                .uri(this.cronBaseUri)
                .retrieve()
                .bodyToMono(String[].class)
                .block();
        logger.info("Ran cron and got");
        logger.info("-----------------");
        for (String message : messages) {
            logger.info(message);
        }
        logger.info("-----------------");
    }
}
