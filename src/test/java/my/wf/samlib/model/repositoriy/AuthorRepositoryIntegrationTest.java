package my.wf.samlib.model.repositoriy;


import my.wf.samlib.TestConfig;
import my.wf.samlib.config.MainConfig;
import my.wf.samlib.helpers.EntityCreator;
import my.wf.samlib.helpers.InitHelper;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.model.entity.Writing;
import org.hamcrest.Matchers;
import org.hibernate.LazyInitializationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {MainConfig.class, TestConfig.class})
public class AuthorRepositoryIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(AuthorRepositoryIntegrationTest.class);

    @Autowired
    InitHelper initHelper;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    CustomerRepository customerRepository;

    Customer defaultCustomer;
    Map<Integer, Author> authors;

    @Before
    @Transactional
    public void setUp() throws Exception {
        authors = initHelper.initAuthors(5);
        defaultCustomer = initHelper.initDefaultCustomer(authors.get(1), authors.get(2), authors.get(3));
        defaultCustomer = customerRepository.getWithFullAuthorList(defaultCustomer.getId());
        defaultCustomer.getUnreadWritings().addAll(authors.get(1).getWritings());
        defaultCustomer.getUnreadWritings().add(authors.get(3).getWritings().iterator().next());
        defaultCustomer = customerRepository.save(defaultCustomer);
        System.out.println("UNREAD:");
        for(Writing writing: defaultCustomer.getUnreadWritings()){
            System.out.println(writing);
        }

    }

    @Test(expected = LazyInitializationException.class)
    public void testGetAuthorWithoutWritings(){
        Assert.assertTrue(authorRepository.findOne(authors.get(0).getId()).getWritings().isEmpty());
    }

    @Test
    public void testGetListByCustomer() throws Exception {
        List<Author> listByCustomer = authorRepository.getListByCustomerId(defaultCustomer.getId());
        Assert.assertThat(listByCustomer, Matchers.hasSize(3));
        Assert.assertThat(listByCustomer,
                Matchers.containsInAnyOrder(authors.get(1), authors.get(2), authors.get(3))
        );
    }

    @Test
    public void testGetUnreadListByCustomer(){
        List<Writing> listByCustomer = authorRepository.getUnreadListByCustomerId(defaultCustomer.getId());
        Assert.assertThat(listByCustomer, Matchers.hasSize(3));
    }

    @Test
    public void testAddWritings(){
        Author author = authorRepository.save(EntityCreator.createAuthor("http://test.writing", "testWriting"));
        Assert.assertThat(author.getWritings(), Matchers.emptyCollectionOf(Writing.class));
        author.getWritings().add(EntityCreator.createWriting("w1", author));
        Author actual = authorRepository.save(author);
        Assert.assertThat(actual.getWritings(),
                Matchers.hasItem(
                        Matchers.hasProperty("name", Matchers.equalTo("w1"))
                )
        );
    }
}