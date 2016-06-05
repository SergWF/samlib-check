package my.wf.samlib.storage.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.wf.samlib.helpers.EntityHelper;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Writing;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import sun.plugin2.applet.Applet2ClassLoaderCache;

import java.util.Date;
import java.util.Set;

public class AuthorStorageImplTest {

    private static final Date DATE0 = new Date(2010, 10, 24, 10, 0, 0);
    private static final Date DATE1 = new Date(2011, 10, 24, 10, 0, 0);
    private static final Date DATE2 = new Date(2012, 10, 24, 10, 0, 0);
    private static final Date DATE3 = new Date(2013, 10, 24, 10, 0, 0);
    private static final Date TEST_DATE = new Date(2012, 11, 24, 10, 0, 0);
    private static final String AUTHOR1_LINK = "http://a1/";
    private static final String AUTHOR2_LINK = "http://a2/";
    private static final String AUTHOR3_LINK = "http://a3/";
    private static final String NEW_AUTHOR_LINK = "http://an/";


    @Spy
    private  AuthorStorageImpl authorStorage;
    @Mock
    ObjectMapper objectMapper;
    private Author author1;
    private Author author2;
    private Author author3;
    private Writing writing;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        authorStorage.setStorageFileName("aaa");
        authorStorage.setObjectMapper(objectMapper);
        author1 = EntityHelper.createAuthor(AUTHOR1_LINK, "a1");
        author2 = EntityHelper.createAuthor(AUTHOR2_LINK, "a2");
        author3 = EntityHelper.createAuthor(AUTHOR3_LINK, "a3");

        putAuthor(author1, 1L);
        putAuthor(author2, 2L);
        putAuthor(author3, 3L);

        EntityHelper.createWriting("w11", author1, DATE1);
        writing = EntityHelper.createWriting("w12", author1, DATE2);
        EntityHelper.createWriting("w13", author1, DATE3);

        EntityHelper.createWriting("w21", author2, DATE1);
        EntityHelper.createWriting("w22", author2, DATE2);
        EntityHelper.createWriting("w23", author2, DATE3);

        EntityHelper.createWriting("w31", author3, DATE1);
        EntityHelper.createWriting("w32", author3, DATE2);
    }

    @Test
    public void addAuthorNew() throws Exception {
        Author author = EntityHelper.createAuthor(NEW_AUTHOR_LINK, "an");
        Assert.assertThat(author, Matchers.hasProperty("id", Matchers.nullValue()));
        Assert.assertThat(authorStorage, Matchers.hasProperty("count", Matchers.equalTo(3L)));
        author = authorStorage.addAuthor(author);
        Assert.assertThat(author, Matchers.hasProperty("id", Matchers.notNullValue()));
        Assert.assertNotNull(authorStorage.getById(4L));
        Assert.assertNotNull(authorStorage.findByLink(NEW_AUTHOR_LINK));
        Assert.assertThat(authorStorage, Matchers.hasProperty("count", Matchers.equalTo(4L)));
        Assert.assertTrue(authorStorage.getAuthors().get(4L).equals(author));
        Mockito.verify(authorStorage, Mockito.times(1)).save(Mockito.eq(author));
    }

    @Test
    public void addAuthorWithExistingLink() throws Exception {
        Author author = EntityHelper.createAuthor(AUTHOR1_LINK, "an");
        Assert.assertThat(author, Matchers.hasProperty("id", Matchers.nullValue()));
        Assert.assertThat(authorStorage, Matchers.hasProperty("count", Matchers.equalTo(3L)));
        author = authorStorage.addAuthor(author);
        Mockito.verify(authorStorage, Mockito.never()).save(Mockito.any(Author.class));
        Assert.assertTrue(authorStorage.getAuthors().get(1L).equals(author));
        Assert.assertThat(author,
                Matchers.allOf(
                        Matchers.hasProperty("link", Matchers.equalTo(AUTHOR1_LINK)),
                        Matchers.hasProperty("name", Matchers.equalTo(author1.getName()))
                )
        );
    }

    @Test
    public void saveNew() throws Exception {
        Author author = EntityHelper.createAuthor(NEW_AUTHOR_LINK, "an");
        long count = authorStorage.getAuthors().size();
        author = authorStorage.save(author);
        Assert.assertEquals(count + 1, authorStorage.getAuthors().size());
        Long id = author.getId();
        Assert.assertNotNull(id);
        Assert.assertTrue(authorStorage.getAuthors().containsKey(id));
        Assert.assertThat(authorStorage.getAuthors().get(id),
                Matchers.allOf(
                        Matchers.hasProperty("id", Matchers.equalTo(id)),
                        Matchers.hasProperty("link", Matchers.equalTo(NEW_AUTHOR_LINK))
                )
        );
    }

    @Test
    public void saveWithExistingId() throws Exception {
        String newName = "new_name";
        Author author = EntityHelper.createAuthor(AUTHOR1_LINK, newName);
        author.setId(author1.getId());
        long count = authorStorage.getAuthors().size();
        authorStorage.save(author);
        Assert.assertEquals(count, authorStorage.getAuthors().size());
        Assert.assertTrue(authorStorage.getAuthors().containsKey(author1.getId()));
        Assert.assertThat(authorStorage.getAuthors().get(author1.getId()),
                Matchers.allOf(
                        Matchers.hasProperty("id", Matchers.equalTo(author1.getId())),
                        Matchers.hasProperty("name", Matchers.equalTo(newName)),
                        Matchers.hasProperty("link", Matchers.equalTo(AUTHOR1_LINK))
                )
        );
    }

    @Test
    public void changeAuthorWithoutSaving() throws Exception {
        Author author = authorStorage.getById(author1.getId());
        String newName = "new_name";
        author.setName(newName);
        Assert.assertThat(authorStorage.getAuthors().get(author1.getId()),
                Matchers.allOf(
                        Matchers.hasProperty("id", Matchers.equalTo(author1.getId())),
                        Matchers.hasProperty("name", Matchers.equalTo(newName))
                )
        );
        Mockito.verify(authorStorage, Mockito.never()).save(Mockito.any(Author.class));
    }

    @Test
    public void deleteExists() throws Exception {
        Assert.assertTrue(authorStorage.getAuthors().contains(author1));
        authorStorage.delete(author1);
        Mockito.verify(authorStorage).saveToPhysicalStorage();
        Assert.assertFalse(authorStorage.getAuthors().containsKey(author1.getId()));
        Assert.assertFalse(authorStorage.getAuthors().contains(author1));
    }

    @Test
    public void deleteNonExists() throws Exception {
        Author author = new Author();
        author.setId(1000L);
        authorStorage.delete(author);
        Mockito.verify(authorStorage, Mockito.never()).saveToPhysicalStorage();
    }

    @Test
    public void findByLink() throws Exception {
        Assert.assertEquals(author1, authorStorage.findByLink(AUTHOR1_LINK));
    }

    @Test
    public void findByLinkNotExists() throws Exception {
        Assert.assertEquals(author1, authorStorage.findByLink(AUTHOR1_LINK));
    }

    @Test
    public void getById() throws Exception {
        Assert.assertEquals(author1, authorStorage.getById(author1.getId()));
    }

    @Test
    public void getByIdNonExists() throws Exception {
        Assert.assertNull(authorStorage.getById(1000l));
    }

    @Test
    public void getAll() throws Exception {
        Assert.assertThat(authorStorage.getAll(), Matchers.hasSize(3));
        Assert.assertThat(authorStorage.getAll(), Matchers.contains(author1, author2, author3));
    }

    @Test
    public void findWritingByLink() throws Exception {
        Assert.assertEquals(writing, authorStorage.findWritingByLink(author1.getLink(), writing.getLink()));
    }

    @Test
    public void findWritingByLinkWrongAuthorLink() throws Exception {
        Assert.assertNull(authorStorage.findWritingByLink("aaa", writing.getLink()));
    }

    @Test
    public void findWritingByLinkWrongWritingLink() throws Exception {
        Assert.assertNull(authorStorage.findWritingByLink(author1.getLink(), "aaaaa"));
    }

    @Test
    public void getUpdatedAfter() throws Exception {
        Set<Author> updatedAfter = authorStorage.getUpdatedAfter(TEST_DATE);
        Assert.assertThat(updatedAfter, Matchers.contains(author1, author2));
    }

    @Test
    public void getUpdatedAfterInTime() throws Exception {
        Author author4 = EntityHelper.createAuthor("http://a4", "a4");
        EntityHelper.createWriting("w41", author4, DATE0);
        putAuthor(author4, 4L);
        Set<Author> updatedAfter = authorStorage.getUpdatedAfter(DATE2);
        Assert.assertThat(updatedAfter, Matchers.contains(author1, author2, author3));
    }

    private void putAuthor(Author author, Long id){
        author.setId(id);
        authorStorage.getAuthors().put(author.getId(), author);
    }

}