package my.wf.samlib.service.impl;

import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.model.repositoriy.AuthorRepository;
import my.wf.samlib.model.repositoriy.CustomerRepository;
import my.wf.samlib.service.SamlibService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.HashSet;

@Service
public class SamlibServiceImpl implements SamlibService {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    AuthorRepository authorRepository;

    @PostConstruct
    public void initDefaults(){
        getDefaultCustomer();
    }

    @Override
    @Transactional
    public Customer getDefaultCustomer() {
        Customer defaultCustomer = customerRepository.findOne(1L);
        if(null  == defaultCustomer){
            defaultCustomer = customerRepository.save(createCustomerInstance("default"));
        }
        return defaultCustomer;
    }

    Customer createCustomerInstance(String customerName){
        Customer customer = new Customer();
        customer.setName(customerName);
        customer.setAuthors(new HashSet<>(authorRepository.findAll()));
        return customer;
    }
}
