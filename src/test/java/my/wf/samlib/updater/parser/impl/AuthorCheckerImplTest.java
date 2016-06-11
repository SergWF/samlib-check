package my.wf.samlib.updater.parser.impl;

import my.wf.samlib.helpers.EntityHelper;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Changed;
import my.wf.samlib.model.entity.Writing;
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
import java.util.Date;
import java.util.List;

public class AuthorCheckerImplTest {
    private static final LocalDateTime CHECK_DATE = LocalDateTime.of(2011, 11, 15, 10, 0, 0);
    private static final LocalDateTime BEFORE_CHECK_DATE =LocalDateTime.of(2010, 11, 15, 10, 0, 0);

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
        authorChecker.setBanCheckUrl("http://1");
        authorChecker.setLinkSuffix("aaa");
        authorChecker.setSamlibAuthorParser(samlibAuthorParser);
        authorChecker.setSamlibPageReader(samlibPageReader);

        author = EntityHelper.createAuthor("a", "a");
    }

    @Test
    public void checkAuthorUpdates() throws Exception {

    }

    @Test
    public void checkIpState() throws Exception {

    }

    @Test
    public void implementChangesNoChanges() throws Exception {
        Writing w1 = EntityHelper.createWriting("w1", author, BEFORE_CHECK_DATE);
        Writing w2 = EntityHelper.createWriting("w2", author, BEFORE_CHECK_DATE);
        Writing w3 = EntityHelper.createWriting("w3", author, BEFORE_CHECK_DATE);
        List<Writing> parsedWritings = Arrays.asList(
                EntityHelper.makeCopy(w1),
                EntityHelper.makeCopy(w2),
                EntityHelper.makeCopy(w3)
        );
        String newName = "new name";
        author = authorChecker.implementChanges(author, parsedWritings, newName, CHECK_DATE);
        Assert.assertThat(author,
                Matchers.allOf(
                        Matchers.hasProperty("name", Matchers.equalTo(newName)),
                        Matchers.hasProperty("writings", Matchers.hasSize(3)),
                        Matchers.hasProperty("unread", Matchers.equalTo(0L)),
                        Matchers.hasProperty("lastChangedDate", Matchers.equalTo(BEFORE_CHECK_DATE))
                )
        );
    }

    @Test
    public void implementChangesUpdated() throws Exception {
        Writing w1 = EntityHelper.createWriting("w1", author, BEFORE_CHECK_DATE);
        Writing w2 = EntityHelper.createWriting("w2", author, BEFORE_CHECK_DATE);
        Writing w3 = EntityHelper.createWriting("w3", author, BEFORE_CHECK_DATE);

        Writing w2Updated = EntityHelper.makeCopy(w2);
        w2Updated.setSize("20k");

        List<Writing> parsedWritings = Arrays.asList(
                EntityHelper.makeCopy(w1),
                EntityHelper.makeCopy(w2Updated),
                EntityHelper.makeCopy(w3)
        );
        String newName = "new name";
        author = authorChecker.implementChanges(author, parsedWritings, newName, CHECK_DATE);
        Assert.assertThat(author,
                Matchers.allOf(
                        Matchers.hasProperty("name", Matchers.equalTo(newName)),
                        Matchers.hasProperty("writings", Matchers.hasSize(3)),
                        Matchers.hasProperty("unread", Matchers.equalTo(1L)),
                        Matchers.hasProperty("lastChangedDate", Matchers.equalTo(CHECK_DATE))
                )
        );
    }

    @Test
    public void implementChangesRemovedWriting() throws Exception {
        Writing w1 = EntityHelper.createWriting("w1", author, BEFORE_CHECK_DATE);
        Writing w2 = EntityHelper.createWriting("w2", author, BEFORE_CHECK_DATE);
        Writing w3 = EntityHelper.createWriting("w3", author, BEFORE_CHECK_DATE);

        Writing w1Parsed = EntityHelper.makeCopy(w1);
        Writing w3Parsed = EntityHelper.makeCopy(w3);
        String newName = "new name";
        author = authorChecker.implementChanges(author, Arrays.asList(w1Parsed, w3Parsed), newName, CHECK_DATE);
        Assert.assertThat(author,
                Matchers.allOf(
                        Matchers.hasProperty("name", Matchers.equalTo(newName)),
                        Matchers.hasProperty("writings", Matchers.hasSize(2)),
                        Matchers.hasProperty("writings", Matchers.containsInAnyOrder(w1Parsed, w3Parsed)),
                        Matchers.hasProperty("unread", Matchers.equalTo(0L)),
                        Matchers.hasProperty("lastChangedDate", Matchers.equalTo(BEFORE_CHECK_DATE))
                )
        );
    }

    @Test
    public void implementChangesEmptyAuthor() throws Exception {
        Writing w1 = EntityHelper.createWriting("w1", null);
        Writing w2 = EntityHelper.createWriting("w2", null);
        Writing w3 = EntityHelper.createWriting("w3", null);
        String newName = "new name";
        author = authorChecker.implementChanges(author, Arrays.asList(w1, w2, w3), newName, CHECK_DATE);
        Assert.assertThat(author,
                Matchers.allOf(
                        Matchers.hasProperty("name", Matchers.equalTo(newName)),
                        Matchers.hasProperty("writings", Matchers.hasSize(3)),
                        Matchers.hasProperty("writings", Matchers.containsInAnyOrder(w1, w2, w3)),
                        Matchers.hasProperty("unread", Matchers.equalTo(3L)),
                        Matchers.hasProperty("lastChangedDate", Matchers.equalTo(CHECK_DATE))
                )
        );
    }

    @Test
    public void applyChangesNew() throws Exception {
        Writing w1 = EntityHelper.createWriting("w1", author);
        Writing w2 = EntityHelper.createWriting("w2", author);
        Writing w3 = EntityHelper.createWriting("w3", author);
        Writing writing = EntityHelper.createWriting("wn", author);

        writing = authorChecker.applyChanges(writing, Arrays.asList(w1, w2, w3), CHECK_DATE);
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
        writing = authorChecker.applyChanges(writing, Arrays.asList(w1, w2, w3), CHECK_DATE);
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
        writing = authorChecker.applyChanges(writing, Arrays.asList(w1, w2, w3), CHECK_DATE);
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