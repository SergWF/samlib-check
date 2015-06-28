package my.wf.samlib.model.repositoriy;

import my.wf.samlib.TestConfig;
import my.wf.samlib.config.MainConfig;
import my.wf.samlib.helpers.InitHelper;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Customer;
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

    Customer customer;
    Map<Integer, Author> authors;


    @Before
    public void setUp() throws Exception {
        authors = initHelper.initAuthors(5);
        customer = initHelper.initCustomer(authors.get(1), authors.get(2), authors.get(3));
    }

    @Test(expected = LazyInitializationException.class)
    public void testGetCustomerWithoutSubscriptions(){
        Assert.assertTrue(customerRepository.findOne(customer.getId()).getSubscriptions().isEmpty());
    }



}