package my.wf.samlib.service.impl;

import my.wf.samlib.model.dto.StatisticDto;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.model.repositoriy.AuthorRepository;
import my.wf.samlib.model.repositoriy.CustomerRepository;
import my.wf.samlib.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
        customer = customerRepository.findOneWithFullData(customer.getId());
        customer.getUnreadWritings().add(writing);
        return customerRepository.save(customer);
    }

    @Override
    public List<Author> getAuthorsList(Customer customer) {
        return new ArrayList<>(customerRepository.findOneWithFullData(customer.getId()).getAuthors());
    }

    @Override
    public Set<Author> getUnreadAuthors(Customer activeCustomer) {
        return customerRepository.findUnreadAuthors(activeCustomer.getId());
    }

    @Override
    public StatisticDto getStatistic(Customer customer) {
        return new StatisticDto(
                authorRepository.count(),
                customerRepository.subscriptionCount(customer.getId()),
                customerRepository.notReadCount(customer.getId()),
                customerRepository.getLastChangedDate(customer.getId())
        );
    }

    @Override
    @Transactional
    public void updateUnreadWritings(Date lastCheckDate) {
        Set<Author> updatedAuthors = authorRepository.findUpdatedAuthors(lastCheckDate);
        for(Author author: updatedAuthors){
            for(Customer customer: author.getSubscribers()){
                customer.getUnreadWritings().addAll(author.getWritings());
                customerRepository.save(customer);
            }
        }

    }
}
