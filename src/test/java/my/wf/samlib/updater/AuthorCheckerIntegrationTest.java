package my.wf.samlib.updater;

import my.wf.samlib.TestConfig;
import my.wf.samlib.config.MainConfig;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.updater.parser.SamlibAuthorParser;
import my.wf.samlib.updater.parser.SamlibPageReader;
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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {MainConfig.class, TestConfig.class})
public class AuthorCheckerIntegrationTest {


    public static final String FULL_AUTHOR_LINK = "http://some_author/indextitle.shtml";
    public static final String SHORT_AUTHOR_LINK = "http://some_author/";
    @Autowired
    @InjectMocks
    AuthorChecker authorChecker;

    @Mock
    SamlibPageReader samlibPageReader;
    @Mock
    SamlibAuthorParser samlibAuthorParser;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUpdateByFullLink() throws Exception {
        Author author = new Author();
        author.setLink(FULL_AUTHOR_LINK);
        authorChecker.checkUpdates(author);
        Mockito.verify(samlibPageReader).readPage(FULL_AUTHOR_LINK);
    }

    @Test
    public void testUpdateByShortLink() throws Exception {
        Author author = new Author();
        author.setLink(FULL_AUTHOR_LINK);
        authorChecker.checkUpdates(author);
        Mockito.verify(samlibPageReader).readPage(SHORT_AUTHOR_LINK);
    }

    @Test
    public void testGetFullAuthorLink() throws Exception {
        Assert.assertEquals(FULL_AUTHOR_LINK, authorChecker.getFullAuthorLink(FULL_AUTHOR_LINK));
        Assert.assertEquals(FULL_AUTHOR_LINK, authorChecker.getFullAuthorLink(SHORT_AUTHOR_LINK));
    }

    @Test
    public void testUpdateNewAuthor() throws Exception {

    }

    @Test
    public void testUpdateExistingAuthor() throws Exception {

    }

    @Test
    public void testUpdateExistingAuthorWithNewWritings() throws Exception {

    }
    @Test
    public void testUpdateExistingAuthorWithRemovedWritings() throws Exception {

    }
}