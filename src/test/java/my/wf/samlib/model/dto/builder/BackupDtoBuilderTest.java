package my.wf.samlib.model.dto.builder;

import my.wf.samlib.helpers.EntityHelper;
import my.wf.samlib.model.dto.backup.AuthorBackupDto;
import my.wf.samlib.model.dto.backup.CustomerBackupDto;
import my.wf.samlib.model.dto.backup.SubscriptionBackupDto;
import my.wf.samlib.model.dto.backup.WritingBackupDto;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.model.entity.Subscription;
import my.wf.samlib.model.entity.Writing;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BackupDtoBuilderTest {

    Customer customer;
    Author author1;
    Author author2;
    Author author3;
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

        customer = EntityHelper.createCustomerWithSubscription("customer 1", author1, author2);

        subscription1 = EntityHelper.findByAuthor(customer.getSubscriptions(), author1);
        EntityHelper.addSubscriptionUnreads(subscription1, w1a1, w2a1);

        subscription2 = EntityHelper.findByAuthor(customer.getSubscriptions(), author2);
        EntityHelper.addSubscriptionUnreads(subscription2, w1a2, w3a2);

    }

    @Test
    public void testCreateSubscriptionBackupDto() {
        SubscriptionBackupDto subscriptionBackupDto = BackupDtoBuilder.createSubscriptionBackupDto(subscription1);
        Assert.assertThat(subscriptionBackupDto,
                Matchers.allOf(
                        Matchers.hasProperty("authorLink", Matchers.equalTo(author1.getLink())),
                        Matchers.hasProperty("unreadWritings", Matchers.hasSize(2)),
                        Matchers.hasProperty("unreadWritings", Matchers.containsInAnyOrder(w1a1.getLink(), w2a1.getLink()))
                )
        );
    }

    @Test
    public void testCreateCustomerBackupDto() {
        CustomerBackupDto customerBackupDto = BackupDtoBuilder.createCustomerBackupDto(customer);
        Assert.assertThat(customerBackupDto,
                Matchers.allOf(
                        Matchers.hasProperty("name", Matchers.equalTo(customer.getName() )),
                        Matchers.hasProperty("subscriptions", Matchers.hasSize(2)),
                        Matchers.hasProperty("subscriptions",
                                Matchers.hasItems(
                                        Matchers.allOf(
                                                Matchers.hasProperty("authorLink", Matchers.equalTo(author1.getLink())),
                                                Matchers.hasProperty("unreadWritings", Matchers.hasSize(2)),
                                                Matchers.hasProperty("unreadWritings", Matchers.containsInAnyOrder(w1a1.getLink(), w2a1.getLink()))
                                        ),
                                        Matchers.allOf(
                                                Matchers.hasProperty("authorLink", Matchers.equalTo(author2.getLink())),
                                                Matchers.hasProperty("unreadWritings", Matchers.hasSize(2)),
                                                Matchers.hasProperty("unreadWritings", Matchers.containsInAnyOrder(w1a2.getLink(), w3a2.getLink()))
                                        )
                                )
                        )
                )
        );
    }

    @Test
    public void testCreateWritingBackupDto(){
        WritingBackupDto writingBackupDto = BackupDtoBuilder.createWritingBackupDto(w1a1);
        Assert.assertThat(writingBackupDto,
                Matchers.allOf(
                        Matchers.hasProperty("link", Matchers.equalTo(w1a1.getLink())),
                        Matchers.hasProperty("name", Matchers.equalTo(w1a1.getName())),
                        Matchers.hasProperty("lastChangedDate", Matchers.equalTo(w1a1.getLastChangedDate())),
                        Matchers.hasProperty("description", Matchers.equalTo(w1a1.getDescription())),
                        Matchers.hasProperty("groupName", Matchers.equalTo(w1a1.getGroupName())),
                        Matchers.hasProperty("prevSize", Matchers.equalTo(w1a1.getPrevSize())),
                        Matchers.hasProperty("size", Matchers.equalTo(w1a1.getSize()))
                )

        );
    }

    @Test
    public void testCreateAuthorBackupDto() {
        AuthorBackupDto authorBackupDto = BackupDtoBuilder.createAuthorBackupDto(author1);
        Assert.assertThat(authorBackupDto,
                Matchers.allOf(
                        Matchers.hasProperty("link", Matchers.equalTo(author1.getLink())),
                        Matchers.hasProperty("name", Matchers.equalTo(author1.getName())),
                        Matchers.hasProperty("writings", Matchers.hasSize(3)),
                        Matchers.hasProperty("writings",
                                Matchers.hasItems(
                                        Matchers.allOf(
                                                Matchers.hasProperty("link", Matchers.equalTo(w1a1.getLink())),
                                                Matchers.hasProperty("name", Matchers.equalTo(w1a1.getName())),
                                                Matchers.hasProperty("lastChangedDate", Matchers.equalTo(w1a1.getLastChangedDate())),
                                                Matchers.hasProperty("description", Matchers.equalTo(w1a1.getDescription())),
                                                Matchers.hasProperty("groupName", Matchers.equalTo(w1a1.getGroupName())),
                                                Matchers.hasProperty("prevSize", Matchers.equalTo(w1a1.getPrevSize())),
                                                Matchers.hasProperty("size", Matchers.equalTo(w1a1.getSize()))
                                        ),
                                        Matchers.allOf(
                                                Matchers.hasProperty("link", Matchers.equalTo(w2a1.getLink())),
                                                Matchers.hasProperty("name", Matchers.equalTo(w2a1.getName())),
                                                Matchers.hasProperty("lastChangedDate", Matchers.equalTo(w2a1.getLastChangedDate())),
                                                Matchers.hasProperty("description", Matchers.equalTo(w2a1.getDescription())),
                                                Matchers.hasProperty("groupName", Matchers.equalTo(w2a1.getGroupName())),
                                                Matchers.hasProperty("prevSize", Matchers.equalTo(w2a1.getPrevSize())),
                                                Matchers.hasProperty("size", Matchers.equalTo(w2a1.getSize()))
                                        )
                                )
                        )
                )

        );
    }
}