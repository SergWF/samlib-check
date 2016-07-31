package my.wf.samlib.updater.parser.impl;

import my.wf.samlib.helpers.EntityHelper;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Changed;
import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.updater.AuthorDelta;
import my.wf.samlib.updater.parser.SamlibAuthorParser;
import my.wf.samlib.updater.parser.SamlibPageReader;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.time.LocalDateTime;
import java.util.Arrays;

public class AuthorCheckerImplTest {


    private static final LocalDateTime CHECK_DATE = LocalDateTime.of(2011, 11, 15, 10, 0, 0);
    private static final LocalDateTime BEFORE_CHECK_DATE =LocalDateTime.of(2010, 11, 15, 10, 0, 0);
    private static final String NEW_NAME = "newName";

    @Spy
    private AuthorCheckerImpl authorChecker;
    private Author author;
    @Mock
    private SamlibAuthorParser samlibAuthorParser;
    @Mock
    private SamlibPageReader samlibPageReader;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        authorChecker.setLinkSuffix("aaa");
        authorChecker.setSamlibAuthorParser(samlibAuthorParser);
        authorChecker.setSamlibPageReader(samlibPageReader);

        author = EntityHelper.createAuthor("a", "a");
    }

    @Test
    public void checkAuthorUpdates() throws Exception {

    }

    @Test
    public void createDelta() throws Exception {
        Writing w1 = EntityHelper.createWriting("w1", author);
        Writing w2 = EntityHelper.createWriting("w2", author);
        Writing w3 = EntityHelper.createWriting("w3", author);
        Writing w4 = EntityHelper.createWriting("w4", author);
        Writing w5 = EntityHelper.createWriting("w5", author);
        Writing w6 = EntityHelper.createWriting("w6", author);
        
        Writing pW1 = EntityHelper.createWriting("uw1", null);
        pW1.setLink(w1.getLink());
        Writing pW2 = EntityHelper.createWriting("uw2", null);
        pW2.setLink(w2.getLink());
        Writing pW3 = EntityHelper.createWriting("w3", null);
        pW3.setLink(w3.getLink());
        Writing pW4 = EntityHelper.createWriting("w4", null);
        pW4.setLink(w4.getLink());
        Writing pW6 = EntityHelper.createWriting("w6", null);
        pW6.setLink(w6.getLink());
        Writing pW7 = EntityHelper.createWriting("nw7", null);
        Writing pW8 = EntityHelper.createWriting("nw8", null);
        AuthorDelta delta = authorChecker.createDelta(author, NEW_NAME, Arrays.asList(pW1, pW3, pW4));
        Assert.assertThat(delta, 
                Matchers.allOf(
                        Matchers.hasProperty("authorName", Matchers.is(NEW_NAME)),
                        Matchers.hasProperty("timestamp", Matchers.notNullValue()),
                        Matchers.hasProperty("newWritings", Matchers.hasSize(2)),
                        Matchers.hasProperty("newWritings", Matchers.containsInAnyOrder(pW7, pW8)),
                        Matchers.hasProperty("updatedWritings", Matchers.hasSize(2)),
                        Matchers.hasProperty("updatedWritings", Matchers.containsInAnyOrder(pW1, pW2)),
                        Matchers.hasProperty("deletedWritings", Matchers.hasSize(1)),
                        Matchers.hasProperty("deletedWritings", Matchers.containsInAnyOrder(w5))
                )
        );
    }

    @Test
    public void processParsed() throws Exception {

    }

    @Test
    public void checkHasChanges() throws Exception {

    }

    @Test
    public void applyChangesNew() throws Exception {
        Writing w1 = EntityHelper.createWriting("w1", author);
        Writing w2 = EntityHelper.createWriting("w2", author);
        Writing w3 = EntityHelper.createWriting("w3", author);
        Writing writing = EntityHelper.createWriting("wn", author);

        writing = authorChecker.applyWritingChanges(writing, Arrays.asList(w1, w2, w3), CHECK_DATE);
        Assert.assertThat(writing,
                Matchers.allOf(
                        Matchers.hasProperty("changesIn", Matchers.contains(Changed.NEW)),
                        Matchers.hasProperty("lastChangedDate", Matchers.equalTo(CHECK_DATE)),
                        Matchers.hasProperty("unread", Matchers.equalTo(true))
                )
        );
    }

    @Test
    public void applyChangesNoUpdates() throws Exception {

        Writing w1 = EntityHelper.createWriting("w1", author);
        Writing w2 = EntityHelper.createWriting("w2", author, BEFORE_CHECK_DATE);
        Writing w3 = EntityHelper.createWriting("w3", author);
        Writing writing = EntityHelper.makeCopy(w2);
        writing = authorChecker.applyWritingChanges(writing, Arrays.asList(w1, w2, w3), CHECK_DATE);
        Assert.assertThat(writing,
                Matchers.allOf(
                        Matchers.hasProperty("changesIn", Matchers.empty()),
                        Matchers.hasProperty("lastChangedDate", Matchers.equalTo(BEFORE_CHECK_DATE)),
                        Matchers.hasProperty("unread", Matchers.equalTo(false))
                )
        );
    }

    @Test
    public void applyChanges() throws Exception {
        LocalDateTime w2Date = LocalDateTime.of(2010, 10, 15, 10, 0, 0);
        Writing w1 = EntityHelper.createWriting("w1", author);
        Writing w2 = EntityHelper.createWriting("w2", author, w2Date);
        Writing w3 = EntityHelper.createWriting("w3", author);
        Writing writing = EntityHelper.makeCopy(w2);
        writing.setSize("20k");
        writing.setDescription("ssssss");
        writing.setName("annn");
        writing = authorChecker.applyWritingChanges(writing, Arrays.asList(w1, w2, w3), CHECK_DATE);
        Assert.assertThat(writing,
                Matchers.allOf(
                        Matchers.hasProperty("changesIn", Matchers.containsInAnyOrder(Changed.DESCRIPTION, Changed.NAME, Changed.SIZE)),
                        Matchers.hasProperty("lastChangedDate", Matchers.equalTo(CHECK_DATE)),
                        Matchers.hasProperty("unread", Matchers.equalTo(true))
                )
        );
    }

    @Test
    public void findSameWriting() throws Exception {
        Writing w1 = EntityHelper.createWriting("w1", author);
        Writing w2 = EntityHelper.createWriting("w2", author);
        Writing w3 = EntityHelper.createWriting("w3", author);
        Writing writing = EntityHelper.createWriting("w2", author);
        Assert.assertEquals(w2, authorChecker.findSameWriting(Arrays.asList(w1, w2, w3), writing));
    }

    @Test
    public void findSameWritingNotFound() throws Exception {
        Writing w1 = EntityHelper.createWriting("w1", author);
        Writing w2 = EntityHelper.createWriting("w2", author);
        Writing w3 = EntityHelper.createWriting("w3", author);
        Writing writing = EntityHelper.createWriting("w999", author);
        Assert.assertNull(authorChecker.findSameWriting(Arrays.asList(w1, w2, w3), writing));
    }

}