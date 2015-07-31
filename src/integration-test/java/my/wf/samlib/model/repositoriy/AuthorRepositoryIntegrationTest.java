package my.wf.samlib.model.repositoriy;


import my.wf.samlib.TestConfig;
import my.wf.samlib.config.MainConfig;
import my.wf.samlib.helpers.EntityHelper;
import my.wf.samlib.helpers.InitHelper;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.model.entity.Writing;
import org.hamcrest.Matchers;
import org.hibernate.LazyInitializationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {MainConfig.class, TestConfig.class})
public class AuthorRepositoryIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(AuthorRepositoryIntegrationTest.class);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    @Autowired
    InitHelper initHelper;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    CustomerRepository customerRepository;


    @Test
    public void testFindByLink() {
        Map<Integer, Author> authors = initHelper.initAuthors(5);
        Assert.assertEquals(authors.get(0), authorRepository.findByLink(authors.get(0).getLink()));
    }

    @Test(expected = LazyInitializationException.class)
    public void testCheckLazyWritings(){
        //GIVEN: Exists some authors
        Map<Integer, Author> authors = initHelper.initAuthors(5);
        //WHEN: try to read an author by default method and access to the writing list
        //THEN: Should be raised LazyInitializationException
        Assert.assertTrue(authorRepository.findOne(authors.get(0).getId()).getWritings().isEmpty());
    }

    @Test
    public void testCheckAuthorWithWritings() throws ParseException {
        //GIVEN: Exists some authors
        Date[] dates = {sdf.parse("2015.03.20 12:33:00"), sdf.parse("2015.03.20 14:00:00"), sdf.parse("2015.03.20 11:12:00")};
        Author author = initHelper.initSingleAuthor(3);
        int i = 0;
        for(Writing writing:author.getWritings()){
            writing.setLastChangedDate(dates[i++]);
        }
        //WHEN: try to read an author with writings and access to the writing list
        //THEN: Writings should be accessible
        Assert.assertEquals(3, authorRepository.findOneWithWritings(author.getId()).getWritings().size());
        //AND: LastChangedDate should be calculated properly
        Assert.assertEquals(sdf.parse("2015.03.20 14:00:00"), author.getLastChangedDate());
    }


    @Test
    public void testFindLinks(){
        Map<Integer, Author> authors = initHelper.initAuthors(3);
        String link0 = authors.get(0).getLink();
        String link1 = authors.get(1).getLink();
        String link2 = authors.get(2).getLink();
        Collection<String> links = authorRepository.findAllAuthorLinks();
        Assert.assertThat(links, Matchers.hasItems(link0, link1, link2));
    }

    @Test
    public void testfindAllWithWritings(){
        //GIVEN: There are some Authors in DB
        Map<Integer, Author> authors = initHelper.initAuthors(4);
        //WHEN: Read findAllWithWritings
        Set<Author> authorsWithWritings = authorRepository.findAllWithWritings();
        //THEN: Authors should be unique in the result set
        Assert.assertEquals(4, authorsWithWritings.size());
        //THEN: Writings should be accessible
        Author author = authorsWithWritings.iterator().next();
        Assert.assertThat(author.getWritings(),Matchers.hasSize(2));

    }

    @Test(expected = LazyInitializationException.class)
    public void testFindAll(){
        Author author1 = EntityHelper.createAuthor("http://1", "test1");
        author1.getWritings().add(EntityHelper.createWriting("w1a1", author1));
        author1.getWritings().add(EntityHelper.createWriting("w2a1", author1));
        author1.getWritings().add(EntityHelper.createWriting("w3a1", author1));
        authorRepository.save(author1);
        List<Author> allWithWritings = authorRepository.findAll();
        int s = allWithWritings.get(0).getWritings().size();
    }

    @Test
    public void testAddWritings(){
        Author author = authorRepository.save(EntityHelper.createAuthor("http://test.writing", "testWriting"));
        Assert.assertThat(author.getWritings(), Matchers.emptyCollectionOf(Writing.class));
        author.getWritings().add(EntityHelper.createWriting("w1", author));
        Author actual = authorRepository.save(author);
        Assert.assertThat(actual.getWritings(),
                Matchers.hasItem(
                        Matchers.hasProperty("name", Matchers.equalTo("w1"))
                )
        );
    }

    @Test
    public void testDeleteAuthor(){
        //GIVEN: existing Author
        String name = UUID.randomUUID().toString();
        Author author = authorRepository.save(EntityHelper.createAuthor("http://" + name, name));
        //AND: Customer with subscription on this author
        Customer customer = customerRepository.save(EntityHelper.createCustomerWithSubscription(UUID.randomUUID().toString(), author));
        //WHEN:
        authorRepository.delete(author.getId());
        //THEN: author should be deleted
        Assert.assertNull(authorRepository.findOne(author.getId()));
        //AND Customer should be exists
        Assert.assertNotNull(customerRepository.findOne(customer.getId()));
    }

    @Test
    public void testFindWritingByLink(){
        String name = UUID.randomUUID().toString();
        Author author1 = authorRepository.save(EntityHelper.createAuthor("http://" + name, name));
        Writing w1 = EntityHelper.createWriting("w1", author1);
        Writing w2 = EntityHelper.createWriting("w2", author1);
        Writing w3 = EntityHelper.createWriting("w3", author1);
        author1 = authorRepository.save(author1);
        name = UUID.randomUUID().toString();
        Author author2 = authorRepository.save(EntityHelper.createAuthor("http://" + name, name));
        Writing w11 = EntityHelper.createWriting("w11", author2);
        Writing w12 = EntityHelper.createWriting("w12", author2);
        Writing w13 = EntityHelper.createWriting("w13", author2);
        author2 = authorRepository.save(author2);

        Writing found = authorRepository.findWritingByLink(author1.getLink(), "http://w2");
        Assert.assertThat(found,
                Matchers.allOf(
                        Matchers.hasProperty("id", Matchers.notNullValue()),
                        Matchers.hasProperty("name", Matchers.equalTo("w2"))
                )
        );


    }


}