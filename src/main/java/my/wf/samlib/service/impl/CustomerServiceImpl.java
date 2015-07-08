package my.wf.samlib.service.impl;

import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.model.repositoriy.CustomerRepository;
import my.wf.samlib.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomerServiceImpl implements CustomerService {


    @Autowired
    CustomerRepository customerRepository;


    @Override
    public Customer getCustomer(long id) {
        return customerRepository.findOne(id);
    }

    @Override
    public Customer getCustomerWithSubscriptions(long id) {
        return customerRepository.findOneWithSubscriptions(id);
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Set<Customer> getAllCustomers() {
        return new HashSet<>(customerRepository.findAll());
    }

    @Override
    public Set<Customer> getAllCustomersWithSubscriptions() {
        return customerRepository.findAllWithSubscriptions();
    }
}
