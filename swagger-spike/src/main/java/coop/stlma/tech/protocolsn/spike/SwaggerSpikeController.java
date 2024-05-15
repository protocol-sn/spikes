package coop.stlma.tech.protocolsn.spike;

import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.LocalDate;
import java.util.List;

@Controller("/swagger-spike")
@Secured(SecurityRule.IS_ANONYMOUS)
public class SwaggerSpikeController {

    @Get(uri="/", produces="text/plain")
    public String index() {
        return "Example Response";
    }

    @Get(uri="/response-obj", produces="application/json")
    public SomeResponseObj responseObj() {
        return new SomeResponseObj("foo", "bar", 42, 42, List.of("a", "b"));
    }

    @Post(uri="/request-obj", produces="application/json")
    public String requestObj(@Body SomeRequestObj obj) {
        return "Received: " + obj;
    }

    @Get(uri="/{someVal}", produces="text/plain")
    public String responseObj2(@PathVariable String someVal) {
        return someVal;
    }

    @Get(uri="/query", produces="text/plain")
    public String responseObj2(@QueryValue @NotNull String someVal,
                                        @QueryValue @NotBlank String requiredVal,
                                        @QueryValue @Parameter(required = false) Integer nullableInt,
                                        @QueryValue @Parameter(required = false) LocalDate nullableDate) {
        return "Received: " + someVal + " " + requiredVal + " " + nullableInt + " " + nullableDate;
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Get(uri="/secured", produces="text/plain")
    @SecurityRequirement(name = "basicAuthentication")
    public String secured() {
        return "secured";
    }
}