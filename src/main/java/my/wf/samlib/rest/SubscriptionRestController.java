package my.wf.samlib.rest;

import io.swagger.annotations.ApiOperation;
import my.wf.samlib.model.dto.AuthorDetailsDto;
import my.wf.samlib.model.dto.SubscriptionDto;
import my.wf.samlib.model.dto.builder.AuthorDtoBuilder;
import my.wf.samlib.model.dto.builder.SubscriptionDtoBuilder;
import my.wf.samlib.model.entity.Subscription;
import my.wf.samlib.model.statistic.SubscriptionStatistic;
import my.wf.samlib.service.AuthorService;
import my.wf.samlib.service.SamlibService;
import my.wf.samlib.service.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(value = "/subscription", produces= MediaType.APPLICATION_JSON_VALUE)
public class SubscriptionRestController {

    private static final Logger logger  = LoggerFactory.getLogger(SubscriptionRestController.class);

    @Autowired
    SubscriptionService subscriptionService;
    @Autowired
    AuthorService authorService;
    @Autowired
    SamlibService samlibService;

    @ApiOperation(value = "Creates a new subscription")
    @RequestMapping(value = "/subscribe/{authorId}", method = RequestMethod.POST)
    public Subscription subscribe(@PathVariable(value = "authorId") long authorId){
        return subscriptionService.subscribe(samlibService.getActiveCustomer(), authorId);
    }

    @ApiOperation(value = "Creates a new subscription")
    @RequestMapping(value = "/subscribe/url", method = RequestMethod.POST)
    public Subscription subscribe(@RequestBody String authorUrl){
        return subscriptionService.addAuthorAndSubscribe(samlibService.getActiveCustomer(), authorUrl);
    }

    @ApiOperation(value = "Returns list of all subscribed Authors")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Set<SubscriptionStatistic> getSubscriptionList(){
        return subscriptionService.getSubscriptionStatisticList(samlibService.getActiveCustomer());
    }

    @ApiOperation(value = "Returns subscription data")
    @RequestMapping(value = "/{subscriptionId}", method = RequestMethod.GET)
    public Subscription getSubscriptionById(@PathVariable(value = "subscriptionId") long subscriptionId){
        return subscriptionService.getSubscriptionById(samlibService.getActiveCustomer(), subscriptionId);
    }

    @ApiOperation(value = "Mark all writings in subscription as read")
    @RequestMapping(value = "/{subscriptionId}/unread", method = RequestMethod.DELETE)
    public void cancelSubscription(@PathVariable(value = "subscriptionId") Long subscriptionId){
        subscriptionService.cancelSubscription(samlibService.getActiveCustomer(), subscriptionId);
    }

    @ApiOperation(value = "Get only Unread Subscriptions")
    @RequestMapping(value = "/{subscriptionId}/unread", method = RequestMethod.GET)
    public Set<Subscription> unreadList(){
        return subscriptionService.getUnreadInSubscription(samlibService.getActiveCustomer());
    }

    @ApiOperation(value = "Mark all writings in subscription as read")
    @RequestMapping(value = "/{subscriptionId}/unread/all", method = RequestMethod.DELETE)
    public SubscriptionDto markAllAsRead(@PathVariable(value = "subscriptionId") Long subscriptionId){
        Subscription subscription = subscriptionService.markAllAsRead(samlibService.getActiveCustomer(), subscriptionId);
        SubscriptionStatistic subscriptionStatistic = subscriptionService.getSubscriptionStatistic(subscription);
        return SubscriptionDtoBuilder.buildDto(subscription, subscriptionStatistic);
    }

    @ApiOperation(value = "Remove writing from unread list in subscription")
    @RequestMapping(value = "/{subscriptionId}/unread/{writingId}", method = RequestMethod.DELETE)
    public AuthorDetailsDto removeFromUnreadList(@PathVariable(value = "subscriptionId") Long subscriptionId, @PathVariable(value = "writingId") Long writingId){
        logger.info("Remove from UnreadList, subscriptionId={}, writing.id={}",subscriptionId, writingId);
        Subscription subscription = subscriptionService.removeWritingFromUnreadList(samlibService.getActiveCustomer(), subscriptionId, writingId);
        return AuthorDtoBuilder.buildDto(authorService.findAuthor(subscription.getAuthor().getId()),subscription);
    }

    @ApiOperation(value = "Add writing to unread list in subscription")
    @RequestMapping(value = "/{subscriptionId}/unread/{writingId}", method = RequestMethod.POST)
    public AuthorDetailsDto addToUnreadList(@PathVariable(value = "subscriptionId") Long subscriptionId, @PathVariable(value = "writingId") Long writingId){
        logger.info("Add to UnreadList, subscriptionId={}, writing.id={}",subscriptionId, writingId);
        Subscription subscription = subscriptionService.addWritingToUnreadList(samlibService.getActiveCustomer(), subscriptionId, writingId);
        return AuthorDtoBuilder.buildDto(authorService.findAuthor(subscription.getAuthor().getId()),subscription);
    }

}
