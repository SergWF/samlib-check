package my.wf.samlib.service.impl;

import my.wf.samlib.TestConfig;
import my.wf.samlib.WebStubConfig;
import my.wf.samlib.config.MainConfig;
import my.wf.samlib.helpers.EntityHelper;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.model.entity.Subscription;
import my.wf.samlib.model.repositoriy.AuthorRepository;
import my.wf.samlib.model.repositoriy.CustomerRepository;
import my.wf.samlib.model.repositoriy.SubscriptionRepository;
import my.wf.samlib.service.UtilsService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {MainConfig.class, TestConfig.class, WebStubConfig.class})
public class UtilsServiceImplIntegrationTest {


    @Autowired
    UtilsService utilsService;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    SubscriptionRepository subscriptionRepository;
    @Autowired
    AuthorRepository authorRepository;


    Customer customer;
    String link1 = "/test_pages/test_page.html";
    String link2 = "/test_pages/test_page2.html";

    @Before
    public void setUp() throws Exception {
        customer = customerRepository.save(EntityHelper.createCustomerWithSubscription(UUID.randomUUID().toString()));
    }

    @Test
    public void testImportAuthors() {
        Integer expectedAuthorsCount = 2;
        Assert.assertEquals(expectedAuthorsCount, utilsService.importAuthors(customer, Arrays.asList(link1, link2)));
        Collection<Author> authors = authorRepository.findAllWithWritings();
        Assert.assertEquals(2, authors.size());
        Collection<Subscription> subscriptions = subscriptionRepository.findAllByCustomerId(customer.getId());
        Assert.assertEquals(2, subscriptions.size());
    }
}