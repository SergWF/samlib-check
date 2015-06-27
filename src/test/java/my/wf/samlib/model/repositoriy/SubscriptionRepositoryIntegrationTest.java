package my.wf.samlib.model.repositoriy;

import my.wf.samlib.TestConfig;
import my.wf.samlib.config.MainConfig;
import my.wf.samlib.helpers.EntityHelper;
import my.wf.samlib.helpers.InitHelper;
import my.wf.samlib.model.entity.*;
import my.wf.samlib.model.statistic.SubscriptionStatistic;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {MainConfig.class, TestConfig.class})
public class SubscriptionRepositoryIntegrationTest {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.S");
    @Autowired
    InitHelper initHelper;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    SubscriptionRepository subscriptionRepository;
    @Autowired
    AuthorRepository authorRepository;


    @Test
    public void testGetSubscriptionStatistic() throws ParseException {
        Date dateMax = SIMPLE_DATE_FORMAT.parse("2015.01.01 12:00:00.0");
        Date dateMin = SIMPLE_DATE_FORMAT.parse("2015.01.01 10:00:00.0");
        String name1 = UUID.randomUUID().toString();
        Author author1 = EntityHelper.createAuthor("http://" + name1, name1);
        EntityHelper.createWriting("w1a1", author1, dateMax);
        EntityHelper.createWriting("w2a1", author1, dateMax);
        EntityHelper.createWriting("w3a1", author1, dateMin);
        EntityHelper.createWriting("w4a1", author1, dateMax);
        EntityHelper.createWriting("w5a1", author1, dateMin);
        author1 = authorRepository.save(author1);
        Customer customer = EntityHelper.createCustomerWithSubscription(UUID.randomUUID().toString());
        customerRepository.save(customer);
        Subscription subscription = EntityHelper.createSubscription(customer, author1);
        EntityHelper.createSubscriptionUnread(subscription, EntityHelper.findByName("w1a1", author1.getWritings()));
        EntityHelper.createSubscriptionUnread(subscription, EntityHelper.findByName("w2a1", author1.getWritings()));
        EntityHelper.createSubscriptionUnread(subscription, EntityHelper.findByName("w4a1", author1.getWritings()));
        subscription = subscriptionRepository.save(subscription);

        Object data = subscriptionRepository.getSubscriptionStatistic(subscription.getId());
        SubscriptionStatistic statistics = new SubscriptionStatistic(data);
        Assert.assertThat(statistics, Matchers.allOf(
                        Matchers.hasProperty("authorId", Matchers.equalTo(author1.getId())),
                        Matchers.hasProperty("unreadCount", Matchers.equalTo(3L)),
                        Matchers.hasProperty("writingCount", Matchers.equalTo(5L)),
                        Matchers.hasProperty("lastUpdateDate", Matchers.greaterThanOrEqualTo(dateMax))
                )
        );
    }

    @Test
    public void testFindByCustomerId() {
        Author author1 = EntityHelper.createAuthor(UUID.randomUUID().toString(), "a1");
        Author author2 = EntityHelper.createAuthor(UUID.randomUUID().toString(), "a2");
        Author author3 = EntityHelper.createAuthor(UUID.randomUUID().toString(), "a3");
        Author author4 = EntityHelper.createAuthor(UUID.randomUUID().toString(), "a4");
        Author author5 = EntityHelper.createAuthor(UUID.randomUUID().toString(), "a5");

        EntityHelper.createWriting("w1a1", author1);
        EntityHelper.createWriting("w2a1", author1);
        EntityHelper.createWriting("w1a2", author2);
        EntityHelper.createWriting("w2a2", author2);
        EntityHelper.createWriting("w3a2", author2);
        EntityHelper.createWriting("w1a3", author3);
        EntityHelper.createWriting("w2a3", author3);
        EntityHelper.createWriting("w3a3", author3);
        EntityHelper.createWriting("w1a4", author4);
        EntityHelper.createWriting("w1a5", author5);
        authorRepository.save(Arrays.asList(author1, author2, author3, author4, author5));

        Customer customer1 = customerRepository.save(EntityHelper.createCustomerWithSubscription(UUID.randomUUID().toString(), author1, author2, author3));
        Customer customer2 = customerRepository.save(EntityHelper.createCustomerWithSubscription(UUID.randomUUID().toString(), author1, author2, author4));

        Subscription subscription1 = subscriptionRepository.findByCustomerAndAuthorWithUnread(customer1.getId(), author1.getId());
        EntityHelper.createSubscriptionUnread(subscription1, EntityHelper.findByName("w1a1", author1.getWritings()));
        Subscription subscription2 = subscriptionRepository.findByCustomerAndAuthorWithUnread(customer1.getId(), author2.getId());
        EntityHelper.createSubscriptionUnread(subscription2, EntityHelper.findByName("w1a2", author2.getWritings()));
        EntityHelper.createSubscriptionUnread(subscription2, EntityHelper.findByName("w2a2", author2.getWritings()));
        EntityHelper.createSubscriptionUnread(subscription2, EntityHelper.findByName("w3a2", author2.getWritings()));

        customerRepository.save(customer1);
        customerRepository.save(customer2);




        System.out.println("===call start subscriptionRepository.findAllByCustomerId ====");
        Set<Subscription> subscriptionList = subscriptionRepository.findAllByCustomerId(customer1.getId());
        System.out.println("===call end subscriptionRepository.findAllByCustomerId ====");

        System.out.println("1:=======");
        for(Subscription subscription: subscriptionList){
            System.out.println(subscription);
        }

        Assert.assertThat(subscriptionList, Matchers.hasSize(3));
        Assert.assertThat(subscriptionList, Matchers.hasItems(
                        Matchers.allOf(
                                Matchers.hasProperty("customer", Matchers.equalTo(customer1)),
                                Matchers.hasProperty("author", Matchers.equalTo(author1))
                        ),
                        Matchers.allOf(
                                Matchers.hasProperty("customer", Matchers.equalTo(customer1)),
                                Matchers.hasProperty("author", Matchers.equalTo(author2))
                        ),
                        Matchers.allOf(
                                Matchers.hasProperty("customer", Matchers.equalTo(customer1)),
                                Matchers.hasProperty("author", Matchers.equalTo(author3))
                        )
                )
        );

        subscriptionList = subscriptionRepository.findAllByCustomerId(customer2.getId());

        System.out.println("2:=======");
        for(Subscription subscription: subscriptionList){
            System.out.println(subscription);
        }

        System.out.println("=======");
        Assert.assertThat(subscriptionList, Matchers.hasSize(3));
        Assert.assertThat(subscriptionList, Matchers.hasItems(
                        Matchers.allOf(
                                Matchers.hasProperty("customer", Matchers.equalTo(customer2)),
                                Matchers.hasProperty("author", Matchers.equalTo(author1))
                        ),
                        Matchers.allOf(
                                Matchers.hasProperty("customer", Matchers.equalTo(customer2)),
                                Matchers.hasProperty("author", Matchers.equalTo(author2))
                        ),
                        Matchers.allOf(
                                Matchers.hasProperty("customer", Matchers.equalTo(customer2)),
                                Matchers.hasProperty("author", Matchers.equalTo(author4))
                        )
                )
        );

    }
}