package coop.stlma.tech.protocolsn.spike;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.View;

@Controller
@Secured(SecurityRule.IS_ANONYMOUS)
public class ViewController {

    @Produces(MediaType.TEXT_HTML)
    @View("user")
    @Get
    @Secured(SecurityRule.IS_ANONYMOUS)
    public HttpResponse<?> homePage() {
        return HttpResponse.ok();
    }
}
