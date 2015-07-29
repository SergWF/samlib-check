package my.wf.samlib.service.impl;

import my.wf.samlib.helpers.EntityHelper;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.model.entity.Subscription;
import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.model.repositoriy.SubscriptionUnreadRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class SubscriptionServiceImplTest {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    @Spy
    @InjectMocks
    SubscriptionServiceImpl subscriptionService;
    @Mock
    SubscriptionUnreadRepository subscriptionUnreadRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testAddAllUpdatedToUnreadList() throws ParseException {
        String name = UUID.randomUUID().toString();
        Author author = EntityHelper.createAuthor("http://" + name, name);
        Writing w1 = EntityHelper.createWriting("w1", author, SIMPLE_DATE_FORMAT.parse("2015.01.01 12:00:00"));
        Writing w2 = EntityHelper.createWriting("w2", author, SIMPLE_DATE_FORMAT.parse("2015.01.01 10:00:00"));
        Writing w3 = EntityHelper.createWriting("w3", author, SIMPLE_DATE_FORMAT.parse("2015.01.01 11:00:00"));
        Writing w4 = EntityHelper.createWriting("w4", author, SIMPLE_DATE_FORMAT.parse("2015.01.01 13:00:00"));
        Writing w5 = EntityHelper.createWriting("w5", author, SIMPLE_DATE_FORMAT.parse("2015.01.01 12:00:00"));
        Customer customer = EntityHelper.createCustomerWithSubscription(UUID.randomUUID().toString());
        Subscription subscription = EntityHelper.createSubscription(customer, author);
        subscription.setId(1000L);
        author.setId(1L);
        w1.setId(100L);
        w2.setId(200L);
        w3.setId(300L);
        w4.setId(400L);
        w5.setId(500L);
        subscriptionService.addAllUpdatedToUnreadList(subscription, author.getWritings(), SIMPLE_DATE_FORMAT.parse("2015.01.01 12:00:00"));
        Assert.assertThat(subscription, Matchers.allOf(
                        Matchers.hasProperty("customer", Matchers.equalTo(customer)),
                        Matchers.hasProperty("author", Matchers.equalTo(author)),
                        Matchers.hasProperty("subscriptionUnreads", Matchers.hasSize(3)),
                        Matchers.hasProperty("subscriptionUnreads", Matchers.hasItems(
                                        Matchers.hasProperty("writing", Matchers.hasProperty("name", Matchers.equalTo("w1"))),
                                        Matchers.hasProperty("writing", Matchers.hasProperty("name", Matchers.equalTo("w4"))),
                                        Matchers.hasProperty("writing", Matchers.hasProperty("name", Matchers.equalTo("w5")))
                                )
                        )
                )
        );
    }
}