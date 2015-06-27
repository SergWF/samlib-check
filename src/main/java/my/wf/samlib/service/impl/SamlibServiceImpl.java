package my.wf.samlib.service.impl;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.model.repositoriy.AuthorRepository;
import my.wf.samlib.model.repositoriy.CustomerRepository;
import my.wf.samlib.service.SamlibService;
import my.wf.samlib.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Collection;

@Service
public class SamlibServiceImpl implements SamlibService {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    SubscriptionService subscriptionService;

    @PostConstruct
    public void initDefaults(){
        getActiveCustomer();
    }

    @Override
    @Transactional
    public Customer getActiveCustomer() {
        Customer defaultCustomer = customerRepository.findOne(1L);
        if(null  == defaultCustomer){
            defaultCustomer = customerRepository.save(createCustomerInstance("default"));
        }
        return defaultCustomer;
    }

    protected Customer createCustomerInstance(String customerName){
        Customer customer = new Customer();
        customer.setName(customerName);
        Collection<Author> authors = authorRepository.findAll();
        for(Author author: authors){
            customer.getSubscriptions().add(subscriptionService.subscribe(customer, author));
        }
        return customer;
    }
}
