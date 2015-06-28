package my.wf.samlib.service;

import my.wf.samlib.model.dto.SubscriptionDto;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.model.entity.Subscription;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface SubscriptionService {
    Subscription subscribe(Customer customer, long authorId);

    Subscription subscribe(Customer customer, Author author);

    Subscription addAuthorAndSubscribe(Customer customer, String authorUrl);

    Set<SubscriptionDto> getSubscriptionList(Customer customer);

    Subscription getSubscriptionById(Customer customer, long subscriptionId);

    Subscription markAllAsRead(Customer customer, Long subscriptionId);

    void cancelSubscription(Customer customer, Long subscriptionId);

    Subscription removeWritingFromUnreadList(Customer customer, Long subscriptionId, Long writingId);

    Subscription addWritingToUnreadList(Customer customer, Long subscriptionId, Long writingId);

    Set<Subscription> getUnreadInSubscription(Customer customer);

    List<Subscription> updateUnreadState(Author author, Date updateDate);
}
