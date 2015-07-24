package my.wf.samlib.service.impl;

import my.wf.samlib.TestConfig;
import my.wf.samlib.WebStubConfig;
import my.wf.samlib.config.MainConfig;
import my.wf.samlib.helpers.EntityHelper;
import my.wf.samlib.model.dto.backup.AuthorBackupDto;
import my.wf.samlib.model.dto.backup.BackupDto;
import my.wf.samlib.model.dto.backup.CustomerBackupDto;
import my.wf.samlib.model.dto.backup.SubscriptionBackupDto;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.service.AuthorService;
import my.wf.samlib.service.CustomerService;
import my.wf.samlib.service.SubscriptionService;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {MainConfig.class, TestConfig.class, WebStubConfig.class})
public class RestoreServiceImplIntegrationTest {

    BackupDto backupDto;

    @Autowired
    RestoreServiceImpl restoreService;
    @Autowired
    AuthorService authorService;
    @Autowired
    SubscriptionService subscriptionService;
    @Autowired
    CustomerService customerService;

    @Before
    public void setUp() throws Exception {
        AuthorBackupDto authorBackupDto1 = EntityHelper.createAuthorBackupDto("http://a1", "a1", "w11", "w12", "w13");
        AuthorBackupDto authorBackupDto2 = EntityHelper.createAuthorBackupDto("http://a2", "a2", "w21", "w22", "w23");
        AuthorBackupDto authorBackupDto3 = EntityHelper.createAuthorBackupDto("http://a3", "a3", "w31", "w32", "w33");
        CustomerBackupDto customerBackupDto1 = EntityHelper.createCustomerBackupDto("customer1");
        SubscriptionBackupDto subscriptionBackupDto1 = EntityHelper.createSubscriptionBackupDto(authorBackupDto1, Arrays.asList("w11", "w13"));
        SubscriptionBackupDto subscriptionBackupDto2 = EntityHelper.createSubscriptionBackupDto(authorBackupDto2, Arrays.asList("w21", "w22"));
        customerBackupDto1.getSubscriptions().addAll(Arrays.asList(subscriptionBackupDto1, subscriptionBackupDto2));
        CustomerBackupDto customerBackupDto2 = EntityHelper.createCustomerBackupDto("customer2");
        SubscriptionBackupDto subscriptionBackupDto3 = EntityHelper.createSubscriptionBackupDto(authorBackupDto1, Arrays.asList("w11", "w13"));
        SubscriptionBackupDto subscriptionBackupDto4 = EntityHelper.createSubscriptionBackupDto(authorBackupDto3, Arrays.asList("w32", "w33"));
        customerBackupDto1.getSubscriptions().addAll(Arrays.asList(subscriptionBackupDto3, subscriptionBackupDto4));
        backupDto = new BackupDto();
        backupDto.setCreateDate(new Date());
        backupDto.getCustomers().addAll(Arrays.asList(customerBackupDto1, customerBackupDto2));
        backupDto.getAuthors().addAll(Arrays.asList(authorBackupDto1, authorBackupDto2, authorBackupDto3));
    }

    @Test
    public void testRestoreAuthors() throws Exception {
        restoreService.restoreAuthors(backupDto);
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


}
