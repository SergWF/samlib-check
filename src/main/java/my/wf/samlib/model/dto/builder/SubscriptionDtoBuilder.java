package my.wf.samlib.model.dto.builder;

import my.wf.samlib.model.dto.SubscriptionDto;
import my.wf.samlib.model.entity.Subscription;
import my.wf.samlib.model.statistic.SubscriptionStatistic;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class SubscriptionDtoBuilder {


    public static SubscriptionDto buildDto(Subscription subscription, SubscriptionStatistic statistic){
        SubscriptionDto subscriptionDto = new SubscriptionDto();
        subscriptionDto.setSubscriptionId(subscription.getId());
        subscriptionDto.setAuthorId(subscription.getAuthor().getId());
        subscriptionDto.setAuthorName(subscription.getAuthor().getName());
        subscriptionDto.setAuthorLink(subscription.getAuthor().getLink());
        subscriptionDto.setLastUpdateDate(statistic.getLastUpdateDate());
        subscriptionDto.setUnreadCount(statistic.getUnreadCount());
        subscriptionDto.setWritingCount(statistic.getWritingCount());
        return subscriptionDto;
    }

    public static Set<SubscriptionDto> buildDtoSet(Collection<Subscription> subscriptions, Collection<SubscriptionStatistic> statList){
        Set<SubscriptionDto> subscriptionDtoSet = new HashSet<>(subscriptions.size());
        for(SubscriptionStatistic subscriptionStatistic: statList){
            Subscription subscription = findInList(subscriptionStatistic.getSubscriptionId(), subscriptions);
            if(null != subscription) {
                subscriptionDtoSet.add(buildDto(subscription, subscriptionStatistic));
            }
        }
        return subscriptionDtoSet;
    }

    private static Subscription findInList(Long id, Collection<Subscription> subscriptions){
        for(Subscription subscription: subscriptions){
            if(subscription.getId() == id){
                return subscription;
            }
        }
        return null;
    }
}
