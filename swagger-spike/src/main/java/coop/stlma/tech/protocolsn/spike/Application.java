package coop.stlma.tech.protocolsn.spike;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.servers.Server;


@OpenAPIDefinition(info = @Info(
        title = "Swagger Spike",
        version = "0.1"
))
@SecurityScheme(name = "basicAuthentication", type = SecuritySchemeType.HTTP, scheme = "basic")
public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}