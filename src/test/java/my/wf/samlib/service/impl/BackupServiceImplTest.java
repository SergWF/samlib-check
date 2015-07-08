package my.wf.samlib.service.impl;

import my.wf.samlib.helpers.EntityHelper;
import my.wf.samlib.model.dto.backup.AuthorBackupDto;
import my.wf.samlib.model.dto.backup.CustomerBackupDto;
import my.wf.samlib.model.entity.*;
import my.wf.samlib.service.AuthorService;
import my.wf.samlib.service.CustomerService;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

public class BackupServiceImplTest {

    @InjectMocks
    @Spy
    BackupServiceImpl backupService;
    Author author1;
    Author author2;
    Author author3;
    Customer customer1;
    Customer customer2;
    Writing w1a1;
    Writing w2a1;
    Writing w3a1;
    Writing w1a2;
    Writing w2a2;
    Writing w3a2;
    Writing w1a3;
    Writing w2a3;
    Writing w3a3;
    Subscription subscription1;
    Subscription subscription2;
    Subscription subscription3;
    Subscription subscription4;

    @Mock
    AuthorService authorService;
    @Mock
    CustomerService customerService;

    @Before
    public void setUp() throws Exception {
        author1 = EntityHelper.createAuthor("http://author_link1/", "author 1");
        w1a1 = EntityHelper.createWriting("w1a1", author1);
        w2a1 = EntityHelper.createWriting("w2a1", author1);
        w3a1 = EntityHelper.createWriting("w3a1", author1);

        author2 = EntityHelper.createAuthor("http://author_link2/", "author 2");
        w1a2 = EntityHelper.createWriting("w1a2", author2);
        w2a2 = EntityHelper.createWriting("w2a2", author2);
        w3a2 = EntityHelper.createWriting("w3a2", author2);

        author3 = EntityHelper.createAuthor("http://author_link3/", "author 3");
        w1a3 = EntityHelper.createWriting("w1a3", author3);
        w2a3 = EntityHelper.createWriting("w2a3", author3);
        w3a3 = EntityHelper.createWriting("w3a3", author3);

        customer1 = EntityHelper.createCustomerWithSubscription("customer 1", author1, author2);

        subscription1 = EntityHelper.findByAuthor(customer1.getSubscriptions(), author1);
        EntityHelper.addSubscriptionUnreads(subscription1, w1a1, w2a1);

        subscription2 = EntityHelper.findByAuthor(customer1.getSubscriptions(), author2);
        EntityHelper.addSubscriptionUnreads(subscription2, w1a2, w3a2);

        customer2 = EntityHelper.createCustomerWithSubscription("customer 2", author1, author3);

        subscription3 = EntityHelper.findByAuthor(customer2.getSubscriptions(), author1);
        EntityHelper.addSubscriptionUnreads(subscription3, w1a1, w2a1);

        subscription4 = EntityHelper.findByAuthor(customer2.getSubscriptions(), author3);
        EntityHelper.addSubscriptionUnreads(subscription4, w1a3, w3a3);


        MockitoAnnotations.initMocks(this);
        Mockito.doReturn(new HashSet<>(Arrays.asList(customer1, customer2))).when(customerService).getAllCustomersWithSubscriptions();
        Mockito.doReturn(new HashSet<>(Arrays.asList(author1, author2, author3))).when(authorService).findAllAuthorsWithWritings();
    }

    @Test
    public void testRestoreSingleAuthorExistsInDb() throws Exception {
        AuthorBackupDto authorBackupDto = new AuthorBackupDto();
        authorBackupDto.setLink(author1.getLink());
        authorBackupDto.setName(author1.getName());

        backupService.restoreSingleAuthor(authorBackupDto);
    }

    @Test
    public void testRestoreSingleAuthorNotExistsInDb() throws Exception {
        Date date = new Date(new Date().getTime() - 1000000);
        AuthorBackupDto authorBackupDto = new AuthorBackupDto();
        authorBackupDto.setLink("http://newLink");
        authorBackupDto.setName("newName");
        authorBackupDto.getWritings().add(EntityHelper.createWrigingBackupDto("w1an", "10k", date));
        authorBackupDto.getWritings().add(EntityHelper.createWrigingBackupDto("w2an", "20k", date));
        authorBackupDto.getWritings().add(EntityHelper.createWrigingBackupDto("w3an", "30k", date));

        Author restoredAuthor = backupService.restoreSingleAuthor(authorBackupDto);

    }

    @Test
    public void testGetAllAuthors() {
        Collection<AuthorBackupDto> allAuthors = backupService.getAllAuthors();
        Assert.assertThat(allAuthors, Matchers.hasSize(3));
        Assert.assertThat(allAuthors,
                Matchers.hasItems(
                        Matchers.allOf(
                                Matchers.hasProperty("name", Matchers.equalTo(author1.getName())),
                                Matchers.hasProperty("writings", Matchers.hasSize(author1.getWritings().size())),
                                Matchers.hasProperty("writings",
                                        Matchers.hasItems(
                                                Matchers.hasProperty("link", Matchers.equalTo(w1a1.getLink())),
                                                Matchers.hasProperty("link", Matchers.equalTo(w2a1.getLink())),
                                                Matchers.hasProperty("link", Matchers.equalTo(w3a1.getLink()))
                                        )
                                )
                        ),
                        Matchers.allOf(
                                Matchers.hasProperty("name", Matchers.equalTo(author2.getName())),
                                Matchers.hasProperty("writings", Matchers.hasSize(author2.getWritings().size())),
                                Matchers.hasProperty("writings",
                                        Matchers.hasItems(
                                                Matchers.hasProperty("link", Matchers.equalTo(w1a2.getLink())),
                                                Matchers.hasProperty("link", Matchers.equalTo(w2a2.getLink())),
                                                Matchers.hasProperty("link", Matchers.equalTo(w3a2.getLink()))
                                        )
                                )
                        ),
                        Matchers.allOf(
                                Matchers.hasProperty("name", Matchers.equalTo(author3.getName())),
                                Matchers.hasProperty("writings", Matchers.hasSize(author3.getWritings().size())),
                                Matchers.hasProperty("writings",
                                        Matchers.hasItems(
                                                Matchers.hasProperty("link", Matchers.equalTo(w1a3.getLink())),
                                                Matchers.hasProperty("link", Matchers.equalTo(w2a3.getLink())),
                                                Matchers.hasProperty("link", Matchers.equalTo(w3a3.getLink()))
                                        )
                                )
                        )
                )
        );
    }
    @Test
    public void testGetAllCustomers() {
        Collection<CustomerBackupDto> allCustomers = backupService.getAllCustomers();
        Assert.assertThat(allCustomers,
                Matchers.hasItems(
                        Matchers.allOf(
                                Matchers.hasProperty("name", Matchers.equalTo(customer1.getName())),
                                Matchers.hasProperty("subscriptions", Matchers.hasSize(2))
                        ),
                        Matchers.allOf(
                                Matchers.hasProperty("name", Matchers.equalTo(customer2.getName())),
                                Matchers.hasProperty("subscriptions", Matchers.hasSize(2))
                        )
                )
        );
    }

    private void fillData() {
        author1 = EntityHelper.createAuthor("http://author_link1/", "author 1");
        Writing writingA1w1 = EntityHelper.createWriting("a1w1", author1);
        Writing writingA1w2 = EntityHelper.createWriting("a1w2", author1);
        Writing writingA1w3 = EntityHelper.createWriting("a1w3", author1);
        author2 = EntityHelper.createAuthor("http://author_link2/", "author 2");
        Writing writingA2w1 = EntityHelper.createWriting("a2w1", author2);
        Writing writingA2w2 = EntityHelper.createWriting("a2w2", author2);
        Writing writingA2w3 = EntityHelper.createWriting("a2w3", author2);
        author3 = EntityHelper.createAuthor("http://author_link3/", "author 3");
        Writing writingA3w1 = EntityHelper.createWriting("a3w1", author3);
        Writing writingA3w2 = EntityHelper.createWriting("a3w2", author3);
        Writing writingA3w3 = EntityHelper.createWriting("a3w3", author3);
        customer1 = EntityHelper.createCustomerWithSubscription("customer 1", author1, author2);
        Subscription subscription1 = EntityHelper.findByAuthor(customer1.getSubscriptions(), author1);
        SubscriptionUnread subscriptionUnread1 = EntityHelper.createSubscriptionUnread(subscription1, writingA1w1);
        SubscriptionUnread subscriptionUnread2 = EntityHelper.createSubscriptionUnread(subscription1, writingA1w2);
        subscription1.getSubscriptionUnreads().addAll(Arrays.asList(subscriptionUnread1, subscriptionUnread2));
        Subscription subscription2 = EntityHelper.findByAuthor(customer1.getSubscriptions(), author2);
        SubscriptionUnread subscriptionUnread3 = EntityHelper.createSubscriptionUnread(subscription2, writingA2w1);
        SubscriptionUnread subscriptionUnread4 = EntityHelper.createSubscriptionUnread(subscription2, writingA2w2);
        subscription1.getSubscriptionUnreads().addAll(Arrays.asList(subscriptionUnread1, subscriptionUnread2));
        customer2 = EntityHelper.createCustomerWithSubscription("customer 2", author2, author3);
        Subscription subscription3 = EntityHelper.findByAuthor(customer2.getSubscriptions(), author2);
        SubscriptionUnread subscriptionUnread5 = EntityHelper.createSubscriptionUnread(subscription3, writingA2w1);
        SubscriptionUnread subscriptionUnread6 = EntityHelper.createSubscriptionUnread(subscription3, writingA2w3);
        Subscription subscription4 = EntityHelper.findByAuthor(customer2.getSubscriptions(), author3);
        SubscriptionUnread subscriptionUnread7 = EntityHelper.createSubscriptionUnread(subscription4, writingA3w1);
        SubscriptionUnread subscriptionUnread8 = EntityHelper.createSubscriptionUnread(subscription4, writingA3w3);

    }
}