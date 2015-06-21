package my.wf.samlib.updater;

import my.wf.samlib.TestConfig;
import my.wf.samlib.config.MainConfig;
import my.wf.samlib.helpers.EntityHelper;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.model.repositoriy.AuthorRepository;
import my.wf.samlib.updater.parser.SamlibAuthorParser;
import my.wf.samlib.updater.parser.SamlibPageReader;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {MainConfig.class, TestConfig.class})
public class AuthorCheckerIntegrationTest {

    private static final String FULL_AUTHOR_LINK = "http://%s/indextitle.shtml";
    private static final String SHORT_AUTHOR_LINK = "http://%s/";

    @Autowired
    @InjectMocks
    AuthorChecker authorChecker;
    @Mock
    SamlibPageReader samlibPageReader;
    @Autowired
    SamlibAuthorParser samlibAuthorParser;
    @Autowired
    AuthorRepository authorRepository;
    String fullLink;
    String shortLink;
    String authorName;



    @Before
    public void setUp() throws Exception {
        authorName = UUID.randomUUID().toString();
        fullLink =String.format(FULL_AUTHOR_LINK, authorName);
        shortLink =String.format(SHORT_AUTHOR_LINK, authorName);
        MockitoAnnotations.initMocks(this);
        String page = EntityHelper.loadPage("/test_pages/test_page.html");
        Mockito.doReturn(page).when(samlibPageReader).readPage(Mockito.anyString());
    }


    @Test
    public void testUpdateByFullLink() throws Exception {
        Author author = new Author();
        author.setLink(fullLink);
        Author updatedAuthor = authorChecker.checkUpdates(author);
        Mockito.verify(samlibPageReader).readPage(fullLink);
        Assert.assertNotNull(updatedAuthor.getId());
        Assert.assertEquals(shortLink, updatedAuthor.getLink());
    }

    @Test
    public void testUpdateByShortLink() throws Exception {
        Author author = new Author();
        author.setLink(shortLink);
        Author updatedAuthor = authorChecker.checkUpdates(author);
        Mockito.verify(samlibPageReader).readPage(fullLink);
        Assert.assertNotNull(updatedAuthor.getId());
        Assert.assertEquals(shortLink, updatedAuthor.getLink());
    }

    @Test
    public void testCheckNewAuthor() throws Exception {
        Author author = new Author();
        author.setLink(shortLink);
        authorChecker.checkUpdates(author);
        Author updated = authorRepository.findOneByLinkWithWritings(author.getLink());
        Assert.assertThat(updated,
                Matchers.allOf(
                        Matchers.hasProperty("link", Matchers.equalTo(shortLink)),
                        Matchers.hasProperty("name", Matchers.equalTo("Whitefoot Serg")),
                        Matchers.hasProperty("writings", Matchers.hasSize(4))
                )
        );
    }

    @Test
    public void testCheckExistingAuthorNoChanges() throws Exception {
        Author author = new Author();
        author.setLink(shortLink);
        Long id = authorChecker.checkUpdates(author).getId();
        Author existing = authorRepository.findOneByLinkWithWritings(author.getLink());
        Date changedDate = existing.getLastChangedDate();
        Map<String, Date> map = new HashMap<>();
        for(Writing w: existing.getWritings()){
            map.put(w.getLink(), w.getLastChangedDate());
        }
        author = authorChecker.checkUpdates(existing);
        Author newOne = authorRepository.findOneWithWritings(author.getId());
        Assert.assertThat(newOne,
                Matchers.allOf(
                        Matchers.hasProperty("link", Matchers.equalTo(existing.getLink())),
                        Matchers.hasProperty("name", Matchers.equalTo(existing.getName())),
                        Matchers.hasProperty("lastChangedDate", Matchers.equalTo(changedDate)),
                        Matchers.hasProperty("writings", Matchers.hasSize(existing.getWritings().size()))
                )
        );
    }

    @Test
    public void testCheckExistingAuthorNewWriting() throws Exception {
        Author author = new Author();
        author.setLink(shortLink);
        Long id = authorChecker.checkUpdates(author).getId();
        Author existing = authorRepository.findOneByLinkWithWritings(author.getLink());

        Date changedDate = existing.getLastChangedDate();
        int writingsCount = existing.getWritings().size();
        Map<String, Date> map = new HashMap<>();
        Thread.sleep(100);
        String page = EntityHelper.loadPage("/test_pages/test_page_new_writing.html");
        Mockito.doReturn(page).when(samlibPageReader).readPage(Mockito.anyString());

        author = authorChecker.checkUpdates(existing);
        Author newOne = authorRepository.findOneWithWritings(author.getId());
        Assert.assertThat(newOne,
                Matchers.allOf(
                        Matchers.hasProperty("link", Matchers.equalTo(existing.getLink())),
                        Matchers.hasProperty("name", Matchers.equalTo(existing.getName())),
                        Matchers.hasProperty("lastChangedDate", Matchers.greaterThanOrEqualTo(changedDate)),
                        Matchers.hasProperty("writings", Matchers.hasSize(1 + writingsCount)),
                        Matchers.hasProperty("id", Matchers.equalTo(id))

                )
        );
    }

    @Test
    public void testCheckExistingAuthorRemovedWriting() throws Exception {
        Author author = new Author();
        author.setLink(shortLink);
        Long id = authorChecker.checkUpdates(author).getId();
        Author existing = authorRepository.findOneByLinkWithWritings(author.getLink());

        Date changedDate = existing.getLastChangedDate();
        int writingsCount = existing.getWritings().size();
        Map<String, Date> map = new HashMap<>();
        Thread.sleep(100);
        String page = EntityHelper.loadPage("/test_pages/test_page_removed_writing.html");
        Mockito.doReturn(page).when(samlibPageReader).readPage(Mockito.anyString());

        author = authorChecker.checkUpdates(existing);
        Author newOne = authorRepository.findOneWithWritings(author.getId());
        Assert.assertThat(newOne,
                Matchers.allOf(
                        Matchers.hasProperty("link", Matchers.equalTo(existing.getLink())),
                        Matchers.hasProperty("name", Matchers.equalTo(existing.getName())),
                        Matchers.hasProperty("lastChangedDate", Matchers.equalTo(changedDate)),
                        Matchers.hasProperty("writings", Matchers.hasSize(writingsCount - 1)),
                        Matchers.hasProperty("id", Matchers.equalTo(id))

                )
        );
    }

}