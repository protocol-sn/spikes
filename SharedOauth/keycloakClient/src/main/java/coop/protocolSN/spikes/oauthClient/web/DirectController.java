package coop.protocolSN.spikes.oauthClient.web;

import coop.protocolSN.spikes.oauthClient.data.CustomerDAO;
import coop.protocolSN.spikes.oauthClient.domain.Customer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class DirectController {

    private final CustomerDAO customerDAO;

    public DirectController(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @GetMapping("/direct/customers")
    public List<Customer> customers(Principal principal) {
        addCustomers();
        List<Customer> customerList = new ArrayList<>();
        customerDAO.findAll().forEach(customerList::add);
        return customerList;
    }


    public void addCustomers() {

        Customer customer1 = new Customer();
        customer1.setAddress("1111 foo blvd");
        customer1.setName("Foo Industries");
        customer1.setServiceRendered("Important services");
        customerDAO.save(customer1);

        Customer customer2 = new Customer();
        customer2.setAddress("2222 bar street");
        customer2.setName("Bar LLP");
        customer2.setServiceRendered("Important services");
        customerDAO.save(customer2);

        Customer customer3 = new Customer();
        customer3.setAddress("33 main street");
        customer3.setName("Big LLC");
        customer3.setServiceRendered("Important services");
        customerDAO.save(customer3);
    }
}
