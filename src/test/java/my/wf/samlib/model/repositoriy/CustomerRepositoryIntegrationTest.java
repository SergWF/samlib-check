package my.wf.samlib.model.repositoriy;

import my.wf.samlib.TestConfig;
import my.wf.samlib.config.MainConfig;
import my.wf.samlib.helpers.EntityHelper;
import my.wf.samlib.helpers.InitHelper;
import my.wf.samlib.model.entity.*;
import org.hamcrest.Matchers;
import org.hibernate.LazyInitializationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {MainConfig.class, TestConfig.class})
public class CustomerRepositoryIntegrationTest {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    @Autowired
    InitHelper initHelper;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    SubscriptionRepository subscriptionRepository;

    Customer customer1;
    Customer customer2;
    Customer customer3;

    Writing w1a1 ;
    Writing w2a1 ;
    Writing w3a1 ;
    Writing w1a2 ;
    Writing w2a2 ;
    Writing w3a2 ;
    Map<Integer, Author> authors;


    @Before
    public void setUp() throws Exception {
        authors = initHelper.initAuthors(5);
        w1a1 = EntityHelper.findByName("w_" + 1 + "_" + authors.get(1).getName(), authors.get(1).getWritings());
        w2a1 = EntityHelper.findByName("w_" + 2 + "_" + authors.get(1).getName(), authors.get(1).getWritings());
        w3a1 = EntityHelper.findByName("w_" + 3 + "_" + authors.get(1).getName(), authors.get(1).getWritings());
        w1a2 = EntityHelper.findByName("w_" + 1 + "_" + authors.get(2).getName(), authors.get(2).getWritings());
        w2a2 = EntityHelper.findByName("w_" + 2 + "_" + authors.get(2).getName(), authors.get(2).getWritings());
        w3a2 = EntityHelper.findByName("w_" + 3 + "_" + authors.get(2).getName(), authors.get(2).getWritings());
        customer1 = initHelper.initCustomer(authors.get(1), authors.get(2), authors.get(3));
        Subscription subscription = EntityHelper.findByAuthor(customer1.getSubscriptions(), authors.get(1));
        EntityHelper.createSubscriptionUnread(subscription, w1a1);
        EntityHelper.createSubscriptionUnread(subscription, w2a1);
        subscriptionRepository.save(subscription);
        subscription = EntityHelper.findByAuthor(customer1.getSubscriptions(), authors.get(2));
        EntityHelper.createSubscriptionUnread(subscription, w1a2);
        EntityHelper.createSubscriptionUnread(subscription, w3a2);
        subscriptionRepository.save(subscription);
        customer2 = initHelper.initCustomer(authors.get(0), authors.get(2), authors.get(4));
        customer3 = initHelper.initCustomer();
    }

    @Test(expected = LazyInitializationException.class)
    public void testGetCustomerWithoutSubscriptions(){
        Assert.assertTrue(customerRepository.findOne(customer1.getId()).getSubscriptions().isEmpty());
    }

    @Test
    public void testFindOneWithSubscriptions() {
        Customer customer = customerRepository.findOneWithSubscriptions(customer1.getId());
        Assert.assertThat(customer,
                Matchers.allOf(
                        Matchers.hasProperty("name", Matchers.equalTo(customer1.getName())),
                        Matchers.hasProperty("subscriptions", Matchers.hasSize(3)),
                        Matchers.hasProperty("subscriptions",
                                Matchers.hasItems(
                                        Matchers.allOf(
                                                Matchers.hasProperty("author", Matchers.equalTo(authors.get(1))),
                                                Matchers.hasProperty("subscriptionUnreads",
                                                        Matchers.hasItems(
                                                                Matchers.hasProperty("writing", Matchers.equalTo(w1a1)),
                                                                Matchers.hasProperty("writing", Matchers.equalTo(w2a1))
                                                        )
                                                )
                                        ),
                                        Matchers.allOf(
                                                Matchers.hasProperty("author", Matchers.hasProperty("name", Matchers.equalTo(authors.get(2).getName()))),
                                                Matchers.hasProperty("subscriptionUnreads",
                                                        Matchers.hasItems(
                                                                Matchers.hasProperty("writing", Matchers.equalTo(w1a2)),
                                                                Matchers.hasProperty("writing", Matchers.equalTo(w3a2))
                                                        )
                                                )
                                        ),
                                        Matchers.allOf(
                                                Matchers.hasProperty("author", Matchers.hasProperty("name", Matchers.equalTo(authors.get(3).getName()))),
                                                Matchers.hasProperty("subscriptionUnreads", Matchers.emptyCollectionOf(SubscriptionUnread.class))
                                        )
                                )
                        )
                )
        );
    }

    @Test
    public void testFindOneWithSubscriptionsNoUnread() {
        Customer customer = customerRepository.findOneWithSubscriptions(customer2.getId());
        Assert.assertThat(customer,
                Matchers.allOf(
                        Matchers.hasProperty("name", Matchers.equalTo(customer2.getName())),
                        Matchers.hasProperty("subscriptions", Matchers.hasSize(3)),
                        Matchers.hasProperty("subscriptions",
                                Matchers.hasItems(
                                        Matchers.allOf(
                                                Matchers.hasProperty("author", Matchers.hasProperty("name", Matchers.equalTo(authors.get(0).getName()))),
                                                Matchers.hasProperty("subscriptionUnreads", Matchers.emptyCollectionOf(SubscriptionUnread.class))
                                        ),
                                        Matchers.allOf(
                                                Matchers.hasProperty("author", Matchers.hasProperty("name", Matchers.equalTo(authors.get(2).getName()))),
                                                Matchers.hasProperty("subscriptionUnreads", Matchers.emptyCollectionOf(SubscriptionUnread.class))
                                        ),
                                        Matchers.allOf(
                                                Matchers.hasProperty("author", Matchers.hasProperty("name", Matchers.equalTo(authors.get(4).getName()))),
                                                Matchers.hasProperty("subscriptionUnreads", Matchers.emptyCollectionOf(SubscriptionUnread.class))
                                        )
                                )
                        )
                )
        );
    }

    @Test
    public void testFindAllWithSubscriptions() {
        Set<Customer> customers = customerRepository.findAllWithSubscriptions();
        Assert.assertThat(customers,
                Matchers.hasItems(
                        Matchers.allOf(
                                Matchers.hasProperty("name", Matchers.equalTo(customer1.getName())),
                                Matchers.hasProperty("subscriptions", Matchers.hasSize(3)),
                                Matchers.hasProperty("subscriptions",
                                        Matchers.hasItems(
                                                Matchers.allOf(
                                                        Matchers.hasProperty("author", Matchers.equalTo(authors.get(1))),
                                                        Matchers.hasProperty("subscriptionUnreads",
                                                                Matchers.hasItems(
                                                                        Matchers.hasProperty("writing", Matchers.equalTo(w1a1)),
                                                                        Matchers.hasProperty("writing", Matchers.equalTo(w2a1))
                                                                )
                                                        )
                                                ),
                                                Matchers.allOf(
                                                        Matchers.hasProperty("author", Matchers.hasProperty("name", Matchers.equalTo(authors.get(2).getName()))),
                                                        Matchers.hasProperty("subscriptionUnreads",
                                                                Matchers.hasItems(
                                                                        Matchers.hasProperty("writing", Matchers.equalTo(w1a2)),
                                                                        Matchers.hasProperty("writing", Matchers.equalTo(w3a2))
                                                                )
                                                        )
                                                ),
                                                Matchers.allOf(
                                                        Matchers.hasProperty("author", Matchers.hasProperty("name", Matchers.equalTo(authors.get(3).getName()))),
                                                        Matchers.hasProperty("subscriptionUnreads", Matchers.emptyCollectionOf(SubscriptionUnread.class))
                                                )
                                        )
                                )
                        ),
                        Matchers.allOf(
                                Matchers.hasProperty("name", Matchers.equalTo(customer2.getName())),
                                Matchers.hasProperty("subscriptions", Matchers.hasSize(3)),
                                Matchers.hasProperty("subscriptions",
                                        Matchers.hasItems(
                                                Matchers.allOf(
                                                        Matchers.hasProperty("author", Matchers.hasProperty("name", Matchers.equalTo(authors.get(0).getName()))),
                                                        Matchers.hasProperty("subscriptionUnreads", Matchers.emptyCollectionOf(SubscriptionUnread.class))
                                                ),
                                                Matchers.allOf(
                                                        Matchers.hasProperty("author", Matchers.hasProperty("name", Matchers.equalTo(authors.get(2).getName()))),
                                                        Matchers.hasProperty("subscriptionUnreads", Matchers.emptyCollectionOf(SubscriptionUnread.class))
                                                ),
                                                Matchers.allOf(
                                                        Matchers.hasProperty("author", Matchers.hasProperty("name", Matchers.equalTo(authors.get(4).getName()))),
                                                        Matchers.hasProperty("subscriptionUnreads", Matchers.emptyCollectionOf(SubscriptionUnread.class))
                                                )
                                        )
                                )
                        ),
                        Matchers.allOf(
                                Matchers.hasProperty("name", Matchers.equalTo(customer3.getName())),
                                Matchers.hasProperty("subscriptions", Matchers.emptyCollectionOf(Subscription.class))
                        )
                )
        );
    }


}