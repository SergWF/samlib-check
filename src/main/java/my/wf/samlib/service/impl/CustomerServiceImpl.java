package my.wf.samlib.service.impl;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.model.repositoriy.AuthorRepository;
import my.wf.samlib.model.repositoriy.CustomerRepository;
import my.wf.samlib.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    AuthorRepository authorRepository;



    @Override
    public Customer getCustomer(long customerId) {
        return customerRepository.findOne(customerId);
    }

    @Override
    public Customer addAuthor(Customer customer, Author author) {
        customer.getAuthors().add(author);
        return customerRepository.save(customer);
    }

    @Override
    public Customer removeAuthor(Customer customer, Author author) {
        customer.getAuthors().remove(author);
        return customerRepository.save(customer);
    }

    @Override
    public Customer markWritingAsUnread(Customer customer, Writing writing) {
        customer = customerRepository.getWithFullAuthorList(customer.getId());
        customer.getUnreadWritings().add(writing);
        return customerRepository.save(customer);
    }

    @Override
    public List<Author> getAuthorsList(Customer customer) {
        return new ArrayList<>(customerRepository.getWithFullAuthorList(customer.getId()).getAuthors());
    }

    @Override
    public List<Author> getUnreadAuthors(Customer activeCustomer) {
        List<Writing> unreadWritings = authorRepository.getUnreadListByCustomerId(activeCustomer.getId());
        List<Author> authors = new ArrayList<>();
        for(Writing writing: unreadWritings){

        }
        return authors;
    }
}
