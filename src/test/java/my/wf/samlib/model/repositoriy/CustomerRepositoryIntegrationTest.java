package my.wf.samlib.model.repositoriy;

import my.wf.samlib.TestConfig;
import my.wf.samlib.config.MainConfig;
import my.wf.samlib.helpers.EntityHelper;
import my.wf.samlib.helpers.InitHelper;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Customer;
import org.hamcrest.Matchers;
import org.hibernate.LazyInitializationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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

    Customer defaultCustomer;
    Map<Integer, Author> authors;


    @Before
    public void setUp() throws Exception {
        authors = initHelper.initAuthors(5);
        defaultCustomer = initHelper.initCustomer(authors.get(1), authors.get(2), authors.get(3));
    }

    @Test(expected = LazyInitializationException.class)
    public void testGetCustomerWithoutAuthors(){
        Assert.assertTrue(customerRepository.findOne(defaultCustomer.getId()).getAuthors().isEmpty());
    }

    @Test
    public void testSubscriptionCount(){
        Assert.assertTrue(0 <= customerRepository.subscriptionCount(defaultCustomer.getId()));
    }

    @Test
    public void testNotReadCount(){
        Assert.assertTrue(0 <= customerRepository.notReadCount(defaultCustomer.getId()));
    }

    @Test
    public void test(){
        Date date = customerRepository.getLastChangedDate(defaultCustomer.getId());
    }

    @Test
    public void testGetWithFullAuthorList() throws Exception {
        Customer customer = customerRepository.findOneWithFullData(defaultCustomer.getId());
        Assert.assertThat(customer.getAuthors().iterator().next().getWritings(), Matchers.hasSize(2));
    }

    @Test
    public void testFindUnreadAuthors() throws ParseException {
        //GIVEN: There are some authors in DB
        //AND: Some of them are updated after specified date
        String name1 = UUID.randomUUID().toString();
        String name2 = UUID.randomUUID().toString();
        String name3 = UUID.randomUUID().toString();
        Author author1 = EntityHelper.createAuthor("http://" + name1, name1);
        author1.getWritings().add(EntityHelper.createWriting("w1", author1, sdf.parse("2015.02.20 10:00:00")));
        author1.getWritings().add(EntityHelper.createWriting("w2", author1, sdf.parse("2015.02.20 11:00:00")));
        author1.getWritings().add(EntityHelper.createWriting("w3", author1, sdf.parse("2015.02.20 12:10:00")));
        Author author2 = EntityHelper.createAuthor("http://"+name2, name2);
        author2.getWritings().add(EntityHelper.createWriting("w11", author2, sdf.parse("2015.02.20 12:00:00")));
        author2.getWritings().add(EntityHelper.createWriting("w21", author2, sdf.parse("2015.02.20 10:00:00")));
        author2.getWritings().add(EntityHelper.createWriting("w31", author2, sdf.parse("2015.02.20 11:20:00")));
        Author author3 = EntityHelper.createAuthor("http://"+name3, name3);
        author3.getWritings().add(EntityHelper.createWriting("w12", author3, sdf.parse("2015.02.20 10:00:00")));
        author3.getWritings().add(EntityHelper.createWriting("w22", author3, sdf.parse("2015.02.20 10:00:00")));
        author3.getWritings().add(EntityHelper.createWriting("w32", author3, sdf.parse("2015.02.20 11:00:00")));
        author1 =authorRepository.save(author1);
        author2 =authorRepository.save(author2);
        //AND: customer is subscribed to some authors
        Customer customer = EntityHelper.createCustomer(UUID.randomUUID().toString(), author1, author2);
        customer.getUnreadWritings().add(EntityHelper.findByName("w1", author1.getWritings()));
        customer.getUnreadWritings().add(EntityHelper.findByName("w21", author2.getWritings()));
        customer.getUnreadWritings().add(EntityHelper.findByName("w31", author2.getWritings()));
        customer = customerRepository.save(customer);
        //WHEN: ask for updated
        Set<Author> unreadAuthors = customerRepository.findUnreadAuthors(customer.getId());
        //THEN: should return authors that have unread writings for this Customer
        Assert.assertThat(unreadAuthors, Matchers.hasSize(2));
        Assert.assertThat(unreadAuthors,
                Matchers.hasItems(
                        Matchers.allOf(
                                Matchers.hasProperty("id", Matchers.equalTo(author1.getId())),
                                Matchers.hasProperty("writings", Matchers.hasSize(1))
                        ),
                        Matchers.allOf(
                                Matchers.hasProperty("id", Matchers.equalTo(author2.getId())),
                                Matchers.hasProperty("writings", Matchers.hasSize(2))
                        )
                )
        );
    }
}