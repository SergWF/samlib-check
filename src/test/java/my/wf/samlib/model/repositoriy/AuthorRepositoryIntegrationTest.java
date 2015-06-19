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
    public void testGetListByCustomer() throws Exception {
        //GIVEN: There are some Authors in DB
        Map<Integer, Author> authors = initHelper.initAuthors(5);
        //AND: Customer has a subscriptions
        Customer customer = initHelper.initCustomer(authors.get(1), authors.get(2), authors.get(3));
        //AND: Customer has some unread writings
        customer = customerRepository.findOneWithFullData(customer.getId());
        customer.getUnreadWritings().addAll(authors.get(1).getWritings());
        customer.getUnreadWritings().add(authors.get(3).getWritings().iterator().next());
        customerRepository.save(customer);
        //WHEN: Get list by Customer
        List<Author> listByCustomer = authorRepository.getListByCustomerId(customer.getId());
        //THEN: list of author should be accessible
        Assert.assertThat(listByCustomer, Matchers.hasSize(3));
        Assert.assertThat(listByCustomer,
                Matchers.containsInAnyOrder(authors.get(1), authors.get(2), authors.get(3))
        );
    }

    @Test
    public void testGetUnreadListByCustomer(){
        Map<Integer, Author> authors = initHelper.initAuthors(5);
        Customer customer = initHelper.initCustomer(authors.get(1), authors.get(2), authors.get(3));
        customer = customerRepository.findOneWithFullData(customer.getId());
        customer.getUnreadWritings().addAll(authors.get(1).getWritings());
        customer.getUnreadWritings().add(authors.get(3).getWritings().iterator().next());
        customerRepository.save(customer);
        List<Writing> listByCustomer = authorRepository.getUnreadListByCustomerId(customer.getId());
        Assert.assertThat(listByCustomer, Matchers.hasSize(3));
    }

    @Test
    public void testRemoveUnreadWritingFromAuthorEntity(){
        //GIVEN: Author with some writings
        Author author = authorRepository.save(EntityHelper.createAuthor("http://" + UUID.randomUUID().toString(), "some author"));
        author.getWritings().addAll(Arrays.asList(
                EntityHelper.createWriting("w1a1", author),
                EntityHelper.createWriting("w2a1", author),
                EntityHelper.createWriting("forRemove", author))
        );
        author = authorRepository.save(author);
        Writing w1 = getWritingByName("w1a1", author.getWritings());
        Writing w2 = getWritingByName("w2a1", author.getWritings());
        Writing w3 = getWritingByName("forRemove", author.getWritings());
        //AND: Customer, subscribed to the author
        Customer customer = customerRepository.save(EntityHelper.createCustomer("c1", author));
        customer.getUnreadWritings().add(w1);
        customer.getUnreadWritings().add(w3);
        //AND: Customer has unread Writings
        customer = customerRepository.save(customer);
        customer = customerRepository.findOneWithFullData(customer.getId());
        Assert.assertThat(customer.getUnreadWritings(), Matchers.hasSize(2));

        author = authorRepository.findOneWithWritings(author.getId());
        author.getWritings().remove(getWritingByName("forRemove", author.getWritings()));
        author = authorRepository.save(author);
        customer = customerRepository.findOneWithFullData(customer.getId());
        Assert.assertThat(customer.getUnreadWritings(), Matchers.hasSize(1));

    }

    @Test
    public void testFindLinks(){
        String link1 = UUID.randomUUID().toString();
        String link2 = UUID.randomUUID().toString();
        String link3 = UUID.randomUUID().toString();
        authorRepository.save(EntityHelper.createAuthor(link1, "test1"));
        authorRepository.save(EntityHelper.createAuthor(link2, "test2"));
        authorRepository.save(EntityHelper.createAuthor(link3, "test3"));
        List<String> links = authorRepository.findAllAuthorLinks();
        Assert.assertThat(links, Matchers.hasItems(link1, link2, link3));
    }

    @Test
    public void testfindAllWithWritings(){
        //GIVEN: There are some Authors in DB
        Map<Integer, Author> authors = initHelper.initAuthors(4);
        //WHEN: Read findAllWithWritings
        List<Author> authorsWithWritings = authorRepository.findAllWithWritings();
        Author author = authorsWithWritings.get(0);
        //THEN: Writings should be accessible
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
        Customer customer = customerRepository.save(EntityHelper.createCustomer(UUID.randomUUID().toString(), author));
        //WHEN:
        authorRepository.delete(author.getId());
        //THEN: author should be deleted
        Assert.assertNull(authorRepository.findOne(author.getId()));
        //AND Customer should be exists
        Assert.assertNotNull(customerRepository.findOne(customer.getId()));
    }

    @Test
    public void testFindUpdatedAuthors() throws ParseException {
        //GIVEN: There are some authors in DB
        //AND: Some of them are updated after specified date
        String name1 = UUID.randomUUID().toString();
        String name2 = UUID.randomUUID().toString();
        String name3 = UUID.randomUUID().toString();
        Author author1 = EntityHelper.createAuthor("http://"+name1, name1);
        author1.getWritings().add(EntityHelper.createWriting("w1", author1, sdf.parse("2015.02.20 10:00:00")));
        author1.getWritings().add(EntityHelper.createWriting("w2", author1, sdf.parse("2015.02.20 11:00:00")));
        author1.getWritings().add(EntityHelper.createWriting("w3", author1, sdf.parse("2015.02.20 12:10:00")));
        Author author2 = EntityHelper.createAuthor("http://"+name2, name2);
        author2.getWritings().add(EntityHelper.createWriting("w11", author2, sdf.parse("2015.02.20 12:00:00")));
        author2.getWritings().add(EntityHelper.createWriting("w21", author2, sdf.parse("2015.02.20 10:00:00")));
        author2.getWritings().add(EntityHelper.createWriting("w31", author2, sdf.parse("2015.02.20 11:20:00")));
        Author author3 = EntityHelper.createAuthor("http://"+name3, name3);
        author3.getWritings().add(EntityHelper.createWriting("w11", author3, sdf.parse("2015.02.20 10:00:00")));
        author3.getWritings().add(EntityHelper.createWriting("w21", author3, sdf.parse("2015.02.20 10:00:00")));
        author3.getWritings().add(EntityHelper.createWriting("w31", author3, sdf.parse("2015.02.20 11:00:00")));
        author1 =authorRepository.save(author1);
        author2 =authorRepository.save(author2);
        //WHEN: ask for updated
        Set<Author> updatedAuthors = authorRepository.findUpdatedAuthors(sdf.parse("2015.02.20 11:20:00"));
        //THEN: should return authors that have updates with only updated writings
        Assert.assertThat(updatedAuthors, Matchers.hasSize(2));
        Assert.assertThat(updatedAuthors,
                Matchers.hasItems(
                        Matchers.allOf(
                                Matchers.hasProperty("id", Matchers.equalTo(author1.getId())),
                                Matchers.hasProperty("writings", Matchers.hasSize(1))
                        ),
                        Matchers.allOf(
                                Matchers.hasProperty("id", Matchers.equalTo(author2.getId())),
                                Matchers.hasProperty("writings", Matchers.hasSize(2))
                        )
                )
        );

    }

    private Writing getWritingByName(String name, Collection<Writing> writings){
        for(Writing writing: writings){
            if(writing.getName().equals(name)){
                return writing;
            }
        }
        return  null;
    }
}