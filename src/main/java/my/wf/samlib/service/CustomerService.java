package my.wf.samlib.service;

import my.wf.samlib.model.entity.Customer;

import java.util.Set;

public interface CustomerService {

    Customer getCustomer(long id);

    Customer getCustomerWithSubscriptions(long id);

    Customer save(Customer customer);

    Set<Customer> getAllCustomers();

    Set<Customer> getAllCustomersWithSubscriptions();
}
