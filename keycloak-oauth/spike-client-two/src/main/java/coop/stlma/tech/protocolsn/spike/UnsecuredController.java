package coop.stlma.tech.protocolsn.spike;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

@Controller("/unsecured")
@Secured(SecurityRule.IS_ANONYMOUS)
public class UnsecuredController {

    @Get
    public String noRole() {
        return "hit unsecured endpoint!";
    }
}
