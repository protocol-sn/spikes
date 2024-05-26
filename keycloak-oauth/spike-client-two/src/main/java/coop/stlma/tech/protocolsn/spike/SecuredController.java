package coop.stlma.tech.protocolsn.spike;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

@Controller("/secured")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class SecuredController {

    @Secured({"spike-one"})
    @Get("/validRole")
    public String spikeRole() {
        return "hit spike role endpoint!";
    }

    @Secured({"spike-two"})
    @Get("/wrongRole")
    public String spikeRole2() {
        return "hit spike role 2 endpoint!";
    }

    @Get("/noRole")
    public String noRole() {
        return "hit no role endpoint!";
    }
}
