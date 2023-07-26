package coop.protocolSN.spikes.dbMigration.flyway.flywaymigration.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DoStuffController {

    @GetMapping("/read")
    public Object readRecord() {
        return "";
    }
}
