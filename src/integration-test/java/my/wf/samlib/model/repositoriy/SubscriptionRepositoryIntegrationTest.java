package my.wf.samlib.model.repositoriy;

import my.wf.samlib.config.MainConfig;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.model.entity.Subscription;
import my.wf.samlib.model.statistic.SubscriptionStatistic;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import my.wf.samlib.TestConfig;
import my.wf.samlib.helpers.EntityHelper;
import my.wf.samlib.helpers.InitHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

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


    Author author1;
    Author author2;
    Author author3;
    Author author4;
    Author author5;
    Customer customer1;
    Customer customer2;
    Subscription subscription1;
    Subscription subscription2;
    Date dateMax;
    Date dateMin;


    @Before
    public void setUp() throws Exception {
        dateMax = SIMPLE_DATE_FORMAT.parse("2015.01.01 12:00:00.0");
        dateMin = SIMPLE_DATE_FORMAT.parse("2015.01.01 10:00:00.0");

        author1 = EntityHelper.createAuthor(UUID.randomUUID().toString(), "a1");
        author2 = EntityHelper.createAuthor(UUID.randomUUID().toString(), "a2");
        author3 = EntityHelper.createAuthor(UUID.randomUUID().toString(), "a3");
        author4 = EntityHelper.createAuthor(UUID.randomUUID().toString(), "a4");
        author5 = EntityHelper.createAuthor(UUID.randomUUID().toString(), "a5");

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
        author1 = authorRepository.save(author1);
        author2 = authorRepository.save(author2);
        author3 = authorRepository.save(author3);
        author4 = authorRepository.save(author4);
        author5 = authorRepository.save(author5);

        customer1 = customerRepository.save(EntityHelper.createCustomerWithSubscription(UUID.randomUUID().toString(), author1, author2, author3));
        customer2 = customerRepository.save(EntityHelper.createCustomerWithSubscription(UUID.randomUUID().toString(), author1, author2, author4));

        subscription1 = subscriptionRepository.findByCustomerAndAuthorWithUnread(customer1.getId(), author1.getId());
        EntityHelper.createSubscriptionUnread(subscription1, EntityHelper.findByName("w1a1", author1.getWritings()));
        subscription2 = subscriptionRepository.findByCustomerAndAuthorWithUnread(customer1.getId(), author2.getId());
        EntityHelper.createSubscriptionUnread(subscription2, EntityHelper.findByName("w1a2", author2.getWritings()));
        EntityHelper.createSubscriptionUnread(subscription2, EntityHelper.findByName("w2a2", author2.getWritings()));

        subscription1 = subscriptionRepository.save(subscription1);
        subscription2 = subscriptionRepository.save(subscription2);

        customer1 = customerRepository.save(customer1);
        customer2 = customerRepository.save(customer2);

    }

    @Test
    public void testFindByCustomerAndAuthorWithUnread() {
        Long authorId = authorRepository.getOne(author1.getId()).getId();
        Long customerId = customerRepository.findOne(customer1.getId()).getId();
        Subscription subscription = subscriptionRepository.findByCustomerAndAuthorWithUnread(customerId, authorId);
        Assert.assertThat(subscription, Matchers.allOf(
                        Matchers.hasProperty("customer", Matchers.hasProperty("id", Matchers.equalTo(customerId))),
                        Matchers.hasProperty("author", Matchers.hasProperty("id", Matchers.equalTo(authorId)))
                )
        );
    }

    @Test
    public void testGetSubscriptionStatistic() throws ParseException {
        Long authorId = authorRepository.getOne(author2.getId()).getId();
        Long customerId = customerRepository.findOne(customer1.getId()).getId();
        Subscription subscription = subscriptionRepository.findByCustomerAndAuthorWithUnread(customerId, authorId);
        System.out.println("===call start subscriptionRepository.getSubscriptionStatistic ====");
        Object data = subscriptionRepository.getSubscriptionStatistic(subscription.getId());
        System.out.println("===call end   subscriptionRepository.getSubscriptionStatistic ====");
        SubscriptionStatistic statistics = new SubscriptionStatistic(data);
        Assert.assertThat(statistics, Matchers.allOf(
                        Matchers.hasProperty("subscriptionId", Matchers.equalTo(subscription.getId())),
                        Matchers.hasProperty("authorId", Matchers.equalTo(authorId)),
                        Matchers.hasProperty("authorName", Matchers.equalTo(author2.getName())),
                        Matchers.hasProperty("authorLink", Matchers.equalTo(author2.getLink())),
                        Matchers.hasProperty("unreadCount", Matchers.equalTo(2L)),
                        Matchers.hasProperty("writingCount", Matchers.equalTo(3L)),
                        Matchers.hasProperty("lastUpdateDate", Matchers.greaterThanOrEqualTo(dateMax))
                )
        );
    }

    @Test
    public void testFindByCustomerId() {

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