package coop.protocolSN.spikes.oauthClient.oauthClient.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CronController {

	@GetMapping("/cron")
	public String[] getMessages() {
		return new String[] {"alice", "bob", "eve"};
	}
}
