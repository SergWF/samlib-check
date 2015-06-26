package my.wf.samlib.model.repositoriy;

import my.wf.samlib.TestConfig;
import my.wf.samlib.config.MainConfig;
import my.wf.samlib.helpers.EntityHelper;
import my.wf.samlib.helpers.InitHelper;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.model.entity.Subscription;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {MainConfig.class, TestConfig.class})
public class SubscriptionRepositoryTest {

    @Autowired
    InitHelper initHelper;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    SubscriptionRepository subscriptionRepository;
    @Autowired
    AuthorRepository authorRepository;

    @Test
    public void testFindByCustomerId() {
        Map<Integer, Author> authors = initHelper.initAuthors(5);
        Customer customer1 = customerRepository.save(EntityHelper.createCustomer(UUID.randomUUID().toString(), authors.get(0), authors.get(2), authors.get(4)));
        Customer customer2 = customerRepository.save(EntityHelper.createCustomer(UUID.randomUUID().toString(), authors.get(0), authors.get(1), authors.get(3)));

        customer1.getUnreadWritings().addAll(authors.get(0).getWritings());
        customer1.getUnreadWritings().addAll(authors.get(2).getWritings());
        customerRepository.save(customer1);

        customer2.getUnreadWritings().addAll(authors.get(0).getWritings());
        customer2.getUnreadWritings().addAll(authors.get(3).getWritings());
        customerRepository.save(customer2);

        authors.get(0).getWritings().add(EntityHelper.createWriting(UUID.randomUUID().toString(), authors.get(0)));
        authors.get(1).getWritings().add(EntityHelper.createWriting(UUID.randomUUID().toString(), authors.get(1)));
        authors.get(2).getWritings().add(EntityHelper.createWriting(UUID.randomUUID().toString(), authors.get(2)));

        authorRepository.save(authors.values());

        Set<Subscription> subscriptionList = subscriptionRepository.findByCustomerId(customer1.getId());



        System.out.println("1:=======");
        for(Subscription subscription: subscriptionList){
            System.out.println(subscription);
        }

        subscriptionList = subscriptionRepository.findByCustomerId(customer2.getId());

        System.out.println("2:=======");
        for(Subscription subscription: subscriptionList){
            System.out.println(subscription);
        }
    }
}