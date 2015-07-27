package my.wf.samlib.service.impl;

import my.wf.samlib.TestConfig;
import my.wf.samlib.WebStubConfig;
import my.wf.samlib.config.MainConfig;
import my.wf.samlib.helpers.EntityHelper;
import my.wf.samlib.model.dto.backup.AuthorBackupDto;
import my.wf.samlib.model.dto.backup.CustomerBackupDto;
import my.wf.samlib.model.dto.backup.SubscriptionBackupDto;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.model.repositoriy.AuthorRepository;
import my.wf.samlib.service.AuthorService;
import my.wf.samlib.service.CustomerService;
import my.wf.samlib.service.SubscriptionService;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {MainConfig.class, TestConfig.class, WebStubConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RestoreServiceImplIntegrationTest {

    @Autowired
    RestoreServiceImpl restoreService;
    @Autowired
    AuthorService authorService;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    SubscriptionService subscriptionService;
    @Autowired
    CustomerService customerService;

    @Test
    public void testRestoreAuthors() throws Exception {
        Set<AuthorBackupDto> dtos = new HashSet<>();
        dtos.add(EntityHelper.createAuthorBackupDto("http://a1", "a1", "w11", "w12", "w13"));
        dtos.add(EntityHelper.createAuthorBackupDto("http://a2", "a2", "w21", "w22", "w23"));
        dtos.add(EntityHelper.createAuthorBackupDto("http://a3", "a3", "w31", "w32", "w33"));

        restoreService.restoreAuthors(dtos);
        Set<Author> authors = authorService.findAllAuthorsWithWritings();
        Assert.assertThat(authors, Matchers.hasSize(3));
        Assert.assertThat(authors,
                Matchers.hasItems(
                        Matchers.hasProperty("name", Matchers.equalTo("a1")),
                        Matchers.hasProperty("name", Matchers.equalTo("a2")),
                        Matchers.hasProperty("name", Matchers.equalTo("a3"))
                )
        );
    }
    @Test
    public void testRestoreCustomers() throws Exception {
        Author a1 = EntityHelper.createAuthor("http://a1", "a1");
        EntityHelper.createWriting("w11", a1);
        EntityHelper.createWriting("w12", a1);
        EntityHelper.createWriting("w13", a1);
        Author a2 = EntityHelper.createAuthor("http://a2", "a2");
        EntityHelper.createWriting("w21", a2);
        EntityHelper.createWriting("w22", a2);
        EntityHelper.createWriting("w23", a2);
        Author a3 = EntityHelper.createAuthor("http://a3", "a3");
        EntityHelper.createWriting("w31", a3);
        EntityHelper.createWriting("w32", a3);
        EntityHelper.createWriting("w33", a3);
        Author author1 = authorRepository.save(a1);
        Author author2 = authorRepository.save(a2);
        Author author3 = authorRepository.save(a3);


        CustomerBackupDto customerBackupDto1 = EntityHelper.createCustomerBackupDto("customer1");
        SubscriptionBackupDto subscriptionBackupDto1 = EntityHelper.createSubscriptionBackupDto("http://a1", Arrays.asList("http://w11", "http://w13"));
        SubscriptionBackupDto subscriptionBackupDto2 = EntityHelper.createSubscriptionBackupDto("http://a2", Arrays.asList("http://w21", "http://w22"));
        customerBackupDto1.getSubscriptions().addAll(Arrays.asList(subscriptionBackupDto1, subscriptionBackupDto2));

        CustomerBackupDto customerBackupDto2 = EntityHelper.createCustomerBackupDto("customer2");
        SubscriptionBackupDto subscriptionBackupDto3 = EntityHelper.createSubscriptionBackupDto("http://a1", Arrays.asList("http://w11", "http://w13"));
        SubscriptionBackupDto subscriptionBackupDto4 = EntityHelper.createSubscriptionBackupDto("http://a3", Arrays.asList("http://w32", "http://w33"));
        customerBackupDto2.getSubscriptions().addAll(Arrays.asList(subscriptionBackupDto3, subscriptionBackupDto4));

        Set<CustomerBackupDto> dtos = new HashSet<>();
        dtos.addAll(Arrays.asList(customerBackupDto1, customerBackupDto2));
        restoreService.restoreCustomers(dtos);
        Set<Customer> customers = customerService.getAllCustomersWithSubscriptions();
        Assert.assertThat(customers,
                Matchers.hasItems(
                        Matchers.allOf(
                                Matchers.hasProperty("name", Matchers.equalTo("customer1")),
                                Matchers.hasProperty("subscriptions", Matchers.hasSize(2)),
                                Matchers.hasProperty("subscriptions",
                                        Matchers.hasItems(
                                                Matchers.allOf(
                                                        Matchers.hasProperty("author", Matchers.equalTo(author1)),
                                                        Matchers.hasProperty("subscriptionUnreads", Matchers.hasSize(2))
                                                ),
                                                Matchers.allOf(
                                                        Matchers.hasProperty("author", Matchers.equalTo(author2))
//                                                        ,
//                                                        Matchers.hasProperty("subscriptionUnreads", Matchers.hasSize(2))
                                                )
                                        )
                                )

                        ),
                        Matchers.allOf(
                                Matchers.hasProperty("name", Matchers.equalTo("customer2")),
                                Matchers.hasProperty("subscriptions", Matchers.hasSize(2)),
                                Matchers.hasProperty("subscriptions",
                                        Matchers.hasItems(
                                                Matchers.hasProperty("author", Matchers.equalTo(author1)),
                                                Matchers.hasProperty("author", Matchers.equalTo(author3))
                                        )
                                )
                        )
                )
        );
    }


}
