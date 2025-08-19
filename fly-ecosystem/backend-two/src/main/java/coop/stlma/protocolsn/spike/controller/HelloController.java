package coop.stlma.protocolsn.spike.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

@Controller("/")
@ExecuteOn(TaskExecutors.BLOCKING)
public class HelloController {

    @Get
    public String index() {
        return "Hello from the other site!";
    }
}
