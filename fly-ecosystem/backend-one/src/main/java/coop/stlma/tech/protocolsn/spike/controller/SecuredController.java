package coop.stlma.tech.protocolsn.spike.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

@Controller("/secured")
@ExecuteOn(TaskExecutors.BLOCKING)
@Secured(SecurityRule.IS_AUTHENTICATED)
public class SecuredController {

    @Get
    public String secureGet() {
        return "OAuth2 works!";
    }

    @Get("/spike")
    @Secured("spike-two")
    public String secureSpikeGet() {
        return "OAuth2 works with role!";
    }
}
