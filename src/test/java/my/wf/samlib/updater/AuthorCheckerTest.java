package my.wf.samlib.updater;

import my.wf.samlib.helpers.EntityHelper;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.updater.parser.AuthorChangesChecker;
import my.wf.samlib.updater.parser.SamlibAuthorParser;
import my.wf.samlib.updater.parser.SamlibPageReader;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class AuthorCheckerTest {

    @InjectMocks
    private AuthorChecker authorChecker;
    private Author oldAuthor;
    private Author newAuthor;
    @Mock
    private SamlibPageReader samlibPageReader;
    @Mock
    private AuthorChangesChecker authorChangesChecker;
    @Mock
    private SamlibAuthorParser samlibAuthorParser;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        authorChecker = new AuthorChecker();
        authorChecker.setLinkSuffix("index.html");

        oldAuthor = EntityHelper.createAuthor("http://link", "some name");
        oldAuthor.setId(1L);
        oldAuthor.getWritings().add(EntityHelper.createWriting("w1", oldAuthor));
        oldAuthor.getWritings().add(EntityHelper.createWriting("w2", oldAuthor));
        oldAuthor.getWritings().add(EntityHelper.createWriting("w3", oldAuthor));

        newAuthor = EntityHelper.makeCopy(oldAuthor);

        Mockito.doReturn(newAuthor).when(samlibAuthorParser).parse(Mockito.anyString(), Mockito.anyString());
        Mockito.doReturn("").when(samlibPageReader).readPage(Mockito.anyString());
    }

    @Test
    public void testUpdateExistsSameWritings(){
        //GIVEN: Existing author with no changes
        //WHEN: checkUpdates author's data
        Author updatedAuthor = authorChecker.checkUpdates(oldAuthor);
        //THAN: Author data should be same to the old author's data
        Assert.assertThat(updatedAuthor,
                Matchers.allOf(
                        Matchers.hasProperty("id", Matchers.equalTo(oldAuthor.getId())),
                        Matchers.hasProperty("link", Matchers.equalTo(oldAuthor.getLink())),
                        Matchers.hasProperty("name", Matchers.equalTo(oldAuthor.getName())),
                        Matchers.hasProperty("writings", Matchers.hasSize(oldAuthor.getWritings().size()))
                )
        );
        //AND: Writings data should be same
        for(Writing writing: updatedAuthor.getWritings()){
            //Set new Author for simplifying the checking
            Writing oldWriting = EntityHelper.findByLink(writing.getLink(), oldAuthor.getWritings());
            oldWriting.setAuthor(writing.getAuthor());
            Assert.assertThat(">>" + writing.getName() + "===" + oldWriting.getName(), writing, Matchers.samePropertyValuesAs(oldWriting));
        }
    }

    @Test
    public void testUpdateExistsNewWriting(){
        //GIVEN: Existing author with one new Writing
        Writing newWriting = EntityHelper.createWriting("newWriting", newAuthor);
        newWriting.setSize("123k");
        newAuthor.getWritings().add(newWriting);
        Mockito.doReturn(newAuthor).when(samlibAuthorParser).parse(Mockito.eq(oldAuthor.getLink()), Mockito.anyString());
        //WHEN: checkUpdates author's data
        Author updatedAuthor = authorChecker.checkUpdates(oldAuthor);
        //THAN: Author data should be same to the old author's data
        Assert.assertThat(updatedAuthor,
                Matchers.allOf(
                        Matchers.hasProperty("id", Matchers.equalTo(oldAuthor.getId())),
                        Matchers.hasProperty("link", Matchers.equalTo(oldAuthor.getLink())),
                        Matchers.hasProperty("name", Matchers.equalTo(oldAuthor.getName())),
                        Matchers.hasProperty("writings", Matchers.hasSize(1 + oldAuthor.getWritings().size()))
                )
        );
        //AND: Writings data should be same
        for(Writing oldWriting: oldAuthor.getWritings()){
            //Set new Author for simplifying the checking
            Writing writing = EntityHelper.findByLink(oldWriting.getLink(), newAuthor.getWritings());
            Assert.assertNotNull("", newWriting);
            oldWriting.setAuthor(writing.getAuthor());
            Assert.assertThat(">>" + writing.getName() + "===" + oldWriting.getName(), writing, Matchers.samePropertyValuesAs(oldWriting));
        }
        //AND: Writings should contains new Writing
        Writing newUpdatedWriting = EntityHelper.findByLink("http://newWriting", updatedAuthor.getWritings());
        Assert.assertThat(newUpdatedWriting,
                Matchers.allOf(
                        Matchers.hasProperty("id", Matchers.nullValue()),
                        Matchers.hasProperty("link", Matchers.equalTo("http://newWriting")),
                        Matchers.hasProperty("name", Matchers.equalTo("newWriting"))
                )
        );
    }


}