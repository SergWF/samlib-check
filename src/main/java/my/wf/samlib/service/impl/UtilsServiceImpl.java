package my.wf.samlib.service.impl;

import my.wf.samlib.model.dto.StatisticDto;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.model.entity.Subscription;
import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.model.repositoriy.AuthorRepository;
import my.wf.samlib.model.repositoriy.SubscriptionRepository;
import my.wf.samlib.model.repositoriy.SubscriptionUnreadRepository;
import my.wf.samlib.service.AuthorService;
import my.wf.samlib.service.SubscriptionService;
import my.wf.samlib.service.UtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UtilsServiceImpl implements UtilsService {

    @Autowired
    SubscriptionRepository subscriptionRepository;
    @Autowired
    SubscriptionUnreadRepository subscriptionUnreadRepository;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    AuthorService authorService;
    @Autowired
    SubscriptionService subscriptionService;



    @Override
    public StatisticDto getStatistic(Customer customer) {
        return new StatisticDto(
                authorRepository.count(),
                subscriptionRepository.subscriptionCountByCustomerID(customer.getId()),
                subscriptionUnreadRepository.unreadCountByCustomerId(customer.getId()),
                subscriptionRepository.getLastChangedDateByCustomerId(customer.getId())
        );
    }

    @Override
    public Integer importAuthors(Customer customer, Collection<String> authorLinks) {
        for(String authorLink: authorLinks){
            addAuthor(customer, authorService.addAuthor(authorLink));
        }
        return authorLinks.size();
    }

    @Override
    public Set<String> exportAuthors() {
        return authorRepository.findAllAuthorLinks();
    }



    @Override
    @Transactional
    public void updateUnreadWritings(Date lastCheckDate) {
        Set<Writing> updatedWritings = authorRepository.findUpdatedWritings(lastCheckDate);
        for(Writing writing: updatedWritings){
            subscriptionService.updateUnreadState(writing);
        }
    }

    protected Subscription addAuthor(Customer customer, Author author) {
        return subscriptionService.subscribe(customer, author);
    }

}
