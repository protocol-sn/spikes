package coop.protocolSN.spikes.oauthClient.data;

import coop.protocolSN.spikes.oauthClient.domain.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerDAO extends CrudRepository<Customer, Long> {

}
