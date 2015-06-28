package my.wf.samlib.service.impl;

import my.wf.samlib.exception.InvalidAccessException;
import my.wf.samlib.model.dto.SubscriptionDto;
import my.wf.samlib.model.entity.*;
import my.wf.samlib.model.repositoriy.SubscriptionRepository;
import my.wf.samlib.model.repositoriy.SubscriptionUnreadRepository;
import my.wf.samlib.model.statistic.SubscriptionStatistic;
import my.wf.samlib.service.AuthorService;
import my.wf.samlib.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    AuthorService authorService;
    @Autowired
    SubscriptionRepository subscriptionRepository;
    @Autowired
    SubscriptionUnreadRepository subscriptionUnreadRepository;



    @Override
    public Subscription subscribe(Customer customer, long authorId) {
        Author author = authorService.findAuthor(authorId);
        return subscribe(customer, author);
    }

    @Override
    public Subscription subscribe(Customer customer, Author author) {
        Subscription subscription = subscriptionRepository.findByCustomerAndAuthor(customer.getId(), author.getId());
        return (null == subscription)? createAndSaveSubscription(customer, author): subscription;
    }

    protected Subscription createAndSaveSubscription(Customer customer, Author author) {
        Subscription subscription = new Subscription();
        subscription.setAuthor(author);
        subscription.setCustomer(customer);
        subscription.setSubscribedDate(new Date());
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription addAuthorAndSubscribe(Customer customer, String authorUrl) {
        Author author = authorService.addAuthor(authorUrl);
        return subscribe(customer, author);
    }

    @Override
    public Set<SubscriptionDto> getSubscriptionList(Customer customer) {
        Set<Subscription> subscriptions = subscriptionRepository.findAllByCustomerId(customer.getId());
        List<Object> dataList = subscriptionRepository.getAllSubscriptionStatistic(customer.getId());
        Set<SubscriptionDto> subscriptionDtoSet = new HashSet<>(subscriptions.size());
        for(Object data: dataList){
            SubscriptionStatistic stat = new SubscriptionStatistic(data);
            Subscription subscription = findInList(stat.getSubscriptionId(), subscriptions);
            if(null != subscription) {
                subscriptionDtoSet.add(createSubscriptionDto(subscription, stat));
            }
        }
        return subscriptionDtoSet;
    }

    private SubscriptionDto createSubscriptionDto(Subscription subscription, SubscriptionStatistic subscriptionStatistic){
        SubscriptionDto subscriptionDto = new SubscriptionDto();
        subscriptionDto.setSubscriptionId(subscription.getId());
        subscriptionDto.setAuthorId(subscription.getAuthor().getId());
        subscriptionDto.setAuthorName(subscription.getAuthor().getName());
        subscriptionDto.setAuthorLink(subscription.getAuthor().getLink());
        subscriptionDto.setLastUpdateDate(subscriptionStatistic.getLastUpdateDate());
        subscriptionDto.setUnreadCount(subscriptionStatistic.getUnreadCount());
        subscriptionDto.setWritingCount(subscriptionStatistic.getWritingCount());
        return subscriptionDto;
    }

    private Subscription findInList(Long id, Collection<Subscription> subscriptions){
        for(Subscription subscription: subscriptions){
            if(subscription.getId() == id){
                return subscription;
            }
        }
        return null;
    }

    @Override
    public Subscription getSubscriptionById(Customer customer, long subscriptionId) {
        checkExists(customer.getId(), subscriptionId);
        return subscriptionRepository.findOne(subscriptionId);
    }

    @Override
    public Subscription markAllAsRead(Customer customer, Long subscriptionId) {
        checkExists(customer.getId(), subscriptionId);
        Subscription subscription = subscriptionRepository.findOne(subscriptionId);
        subscription.getSubscriptionUnreads().clear();
        return subscriptionRepository.save(subscription);
    }

    @Override
    public void cancelSubscription(Customer customer, Long subscriptionId) {
        checkExists(customer.getId(), subscriptionId);
        subscriptionRepository.delete(subscriptionId);
    }

    @Override
    public Subscription removeWritingFromUnreadList(Customer customer, Long subscriptionId, Long writingId) {
        checkExists(customer.getId(), subscriptionId);
        Subscription subscription = subscriptionRepository.findOne(subscriptionId);
        SubscriptionUnread subscriptionUnread = subscriptionUnreadRepository.findBySubscriptionIdAndWritingId(subscriptionId, writingId);
        subscription.getSubscriptionUnreads().remove(subscriptionUnread);
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription addWritingToUnreadList(Customer customer, Long subscriptionId, Long writingId) {
        checkExists(customer.getId(), subscriptionId);
        Subscription subscription = subscriptionRepository.findOne(subscriptionId);
        Writing writing = authorService.getWritingById(writingId);
        return subscriptionRepository.save(addToUnreadList(subscription, writing));
    }

    private Subscription addToUnreadList(Subscription subscription, Writing writing) {
        SubscriptionUnread subscriptionUnread = new SubscriptionUnread();
        subscriptionUnread.setSubscription(subscription);
        subscriptionUnread.setWriting(writing);
        subscription.getSubscriptionUnreads().add(subscriptionUnread);
        return subscription;
    }

    protected void addAllUpdatedToUnreadList(Subscription subscription, Collection<Writing> writings, Date checkDate) {
        for(Writing writing: writings){
            if(0 >= checkDate.compareTo(writing.getLastChangedDate())){
                addToUnreadList(subscription, writing);
            }
        }
    }

    @Override
    public Set<Subscription> getUnreadInSubscription(Customer customer) {
        return subscriptionRepository.findUnreadOnly(customer.getId());
    }

    @Override
    public List<Subscription> updateUnreadState(Author author, Date updateDate) {
        if(null == author || updateDate.after(author.getLastChangedDate())){
            return new ArrayList<>();
        }

        Set<Subscription> subscriptions = subscriptionRepository.findAllByAndAuthorId(author.getId());
        for(Subscription subscription: subscriptions){
            addAllUpdatedToUnreadList(subscription, author.getWritings(), updateDate);
        }
        return subscriptionRepository.save(subscriptions);
    }

    protected void checkExists(Long customerId, Long subscriptionId){
        if(!subscriptionRepository.exists(customerId, subscriptionId)){
            throw new InvalidAccessException("Wrong Subscription Id");
        }
    }
}
