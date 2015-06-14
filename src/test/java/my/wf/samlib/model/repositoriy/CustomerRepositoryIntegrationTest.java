package my.wf.samlib.model.repositoriy;

import my.wf.samlib.TestConfig;
import my.wf.samlib.config.MainConfig;
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

import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {MainConfig.class, TestConfig.class})
public class CustomerRepositoryIntegrationTest {

    @Autowired
    InitHelper initHelper;
    @Autowired
    CustomerRepository customerRepository;

    Customer defaultCustomer;
    Map<Integer, Author> authors;


    @Before
    public void setUp() throws Exception {
        authors = initHelper.initAuthors(5);
        defaultCustomer = initHelper.initDefaultCustomer(authors.get(1), authors.get(2), authors.get(3));
    }

    @Test(expected = LazyInitializationException.class)
    public void testGetCustomerWithoutAuthors(){
        Assert.assertTrue(customerRepository.findOne(defaultCustomer.getId()).getAuthors().isEmpty());
    }

    @Test
    public void testGetWithFullAuthorList() throws Exception {
        Customer customer = customerRepository.getWithFullAuthorList(defaultCustomer.getId());
        Assert.assertThat(customer.getAuthors(),
                Matchers.hasItems(
                        Matchers.allOf(
                                Matchers.hasProperty("name", Matchers.equalTo("name_1")),
                                Matchers.hasProperty("writings",
                                        Matchers.hasItems(
                                                Matchers.hasProperty("name", Matchers.equalTo("w1_a1")),
                                                Matchers.hasProperty("name", Matchers.equalTo("w2_a1"))
                                        )
                                )
                        ),
                        Matchers.allOf(
                                Matchers.hasProperty("name", Matchers.equalTo("name_2")),
                                Matchers.hasProperty("writings",
                                        Matchers.hasItems(
                                                Matchers.hasProperty("name", Matchers.equalTo("w1_a2")),
                                                Matchers.hasProperty("name", Matchers.equalTo("w2_a2"))
                                        )
                                )
                        ),
                        Matchers.allOf(
                                Matchers.hasProperty("name", Matchers.equalTo("name_3")),
                                Matchers.hasProperty("writings",
                                        Matchers.hasItems(
                                                Matchers.hasProperty("name", Matchers.equalTo("w1_a3")),
                                                Matchers.hasProperty("name", Matchers.equalTo("w2_a3"))
                                        )
                                )
                        )
                )
        );
    }
}