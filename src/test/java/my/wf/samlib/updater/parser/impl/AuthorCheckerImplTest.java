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
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AuthorCheckerImplTest {


    private static final LocalDateTime CHECK_DATE = LocalDateTime.of(2011, 11, 15, 10, 0, 0);
    private static final LocalDateTime BEFORE_CHECK_DATE =LocalDateTime.of(2010, 11, 15, 10, 0, 0);
    private static final String NEW_NAME = "newName";

    @Spy
    private AuthorCheckerImpl authorChecker;
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



    }

    @Test
    public void createDelta() throws Exception {
        Author author = EntityHelper.createAuthor("a", "a");
        EntityHelper.createWriting("w1", author);
        EntityHelper.createWriting("w2", author);
        EntityHelper.createWriting("w3", author);
        EntityHelper.createWriting("w4", author);
        EntityHelper.createWriting("w5", author);
        EntityHelper.createWriting("w6", author);

        Author updated = EntityHelper.makeCopy(author);
        updated.setName(NEW_NAME);
        Writing uw1 = EntityHelper.findByLink("w1.shtml", updated.getWritings());
        Writing uw2 = EntityHelper.findByLink("w2.shtml", updated.getWritings());
        Writing uw3 = EntityHelper.findByLink("w3.shtml", updated.getWritings());
        Writing uw4 = EntityHelper.findByLink("w4.shtml", updated.getWritings());
        EntityHelper.findByLink("w5.shtml", updated.getWritings());
        EntityHelper.findByLink("w6.shtml", updated.getWritings());

        uw1.setName("new name1");
        uw2.setSize("999k");
        updated.getWritings().remove(uw3);
        updated.getWritings().remove(uw4);
        EntityHelper.createWriting("nw7", updated);
        EntityHelper.createWriting("nw8", updated);

        AuthorDelta delta = authorChecker.createDelta(author, updated.getName(), updated.getWritings());
        Assert.assertThat(delta,
                Matchers.allOf(
                        Matchers.hasProperty("authorName", Matchers.is(NEW_NAME)),
                        Matchers.hasProperty("timestamp", Matchers.notNullValue())
                        )
        );
        Assert.assertThat(delta.getNewWritings(),
                Matchers.containsInAnyOrder(
                        Matchers.hasProperty("link", Matchers.is("nw7.shtml")),
                        Matchers.hasProperty("link", Matchers.is("nw8.shtml"))
                )
        );
        Assert.assertThat(delta.getDeletedWritings(),
                Matchers.containsInAnyOrder(
                        Matchers.hasProperty("link", Matchers.is("w3.shtml")),
                        Matchers.hasProperty("link", Matchers.is("w4.shtml"))
                )
        );
        Assert.assertThat(delta.getUpdatedWritings(),
                Matchers.containsInAnyOrder(
                        Matchers.hasProperty("link", Matchers.is("w1.shtml")),
                        Matchers.hasProperty("link", Matchers.is("w2.shtml"))
                )
        );
    }

    @Test
    public void checkHasChangesPositiveSize() throws Exception {
        Author author = EntityHelper.createAuthor("a", "a");
        EntityHelper.createWriting("w1", author);
        Writing w1 = EntityHelper.createWriting("w1", null);
        w1.setAuthor(author);
        w1.setSize("100k");
        Assert.assertTrue(authorChecker.checkHasChanges(w1, author));
    }

    @Test
    public void checkHasChangesPositiveDescription() throws Exception {
        Author author = EntityHelper.createAuthor("a", "a");
        EntityHelper.createWriting("w1", author);
        Writing w1 = EntityHelper.createWriting("w1", null);
        w1.setAuthor(author);
        w1.setDescription("new descr");
        Assert.assertTrue(authorChecker.checkHasChanges(w1, author));
    }

    @Test
    public void checkHasChangesPositiveName() throws Exception {
        Author author = EntityHelper.createAuthor("a", "a");
        Writing w0 = EntityHelper.createWriting("w1", author);
        Writing w1 = EntityHelper.createWriting("w1", null);
        w1.setAuthor(author);
        w1.setName("new name");
        Assert.assertTrue(authorChecker.checkHasChanges(w1, author));
    }
    @Test
    public void checkHasChangesPositiveAll() throws Exception {
        Author author = EntityHelper.createAuthor("a", "a");
        EntityHelper.createWriting("w1", author);
        Writing w1 = EntityHelper.createWriting("w1", null);
        w1.setAuthor(author);
        w1.setName("new name");
        w1.setSize("100k");
        w1.setDescription("new descr");
        Assert.assertTrue(authorChecker.checkHasChanges(w1, author));
    }

    @Test
    public void checkHasChangesNegative() throws Exception {
        Author author = EntityHelper.createAuthor("a", "a");
        EntityHelper.createWriting("w1", author);
        Writing w1 = EntityHelper.createWriting("w1", null);
        w1.setAuthor(author);
        Assert.assertFalse(authorChecker.checkHasChanges(w1, author));
    }

    @Test
    public void checkIsNewPositive() throws Exception {
        Author author = EntityHelper.createAuthor("a", "a");
        Writing writing = EntityHelper.createWriting("w1", author);
        EntityHelper.createWriting("w2", author);
        Writing w1 = EntityHelper.makeCopy(writing);
        w1.setAuthor(author);
        w1.setLink("nw.shtml");
        Assert.assertTrue(authorChecker.checkIsNew(w1, author));
    }

    @Test
    public void checkIsNewNegative() throws Exception {
        Author author = EntityHelper.createAuthor("a", "a");
        EntityHelper.createWriting("w1", author);
        EntityHelper.createWriting("w2", author);

        Writing w1 = EntityHelper.createWriting("w1", null);
        w1.setAuthor(author);
        Assert.assertFalse(authorChecker.checkIsNew(w1, author));
    }

    @Test
    public void testDeletedPositive() throws Exception {
        Author author = EntityHelper.createAuthor("a", "a");
        Writing w1 = EntityHelper.createWriting("w1", author);
        Writing w2 = EntityHelper.createWriting("w2", author);
        Writing w3 = EntityHelper.createWriting("w3", author);
        Writing uW = EntityHelper.makeCopy(w3);
        uW.setAuthor(author);
        Set<Writing> deleted = authorChecker.getDeleted(author, Arrays.asList(uW));
        Assert.assertTrue(deleted.containsAll(Arrays.asList(w1,w2)));
        Assert.assertEquals(2, deleted.size());
    }

    @Test
    public void testDeletedNegative() throws Exception {
        Author author = EntityHelper.createAuthor("a", "a");
        Writing w1 = EntityHelper.createWriting("w1", author);
        Writing w2 = EntityHelper.createWriting("w2", author);
        Writing w3 = EntityHelper.createWriting("w3", author);
        Set<Writing> newWritings = new HashSet<>();
        newWritings.add(EntityHelper.makeCopy(w1));
        newWritings.add(EntityHelper.makeCopy(w2));
        newWritings.add(EntityHelper.makeCopy(w3));

        newWritings.forEach(writing -> writing.setAuthor(author));
        Assert.assertTrue(authorChecker.getDeleted(author, newWritings).isEmpty());
    }


    @Test
    public void applyChangesNew() throws Exception {
        Author author = EntityHelper.createAuthor("a", "a");
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
        Author author = EntityHelper.createAuthor("a", "a");
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

        Author author = EntityHelper.createAuthor("a", "a");
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
        Author author = EntityHelper.createAuthor("a", "a");
        Writing w1 = EntityHelper.createWriting("w1", author);
        Writing w2 = EntityHelper.createWriting("w2", author);
        Writing w3 = EntityHelper.createWriting("w3", author);
        Writing writing = EntityHelper.createWriting("w2", author);
        Assert.assertEquals(w2, authorChecker.findSameWriting(Arrays.asList(w1, w2, w3), writing));
    }

    @Test
    public void findSameWritingNotFound() throws Exception {
        Author author = EntityHelper.createAuthor("a", "a");
        Writing w1 = EntityHelper.createWriting("w1", author);
        Writing w2 = EntityHelper.createWriting("w2", author);
        Writing w3 = EntityHelper.createWriting("w3", author);
        Writing writing = EntityHelper.createWriting("w999", author);
        Assert.assertNull(authorChecker.findSameWriting(Arrays.asList(w1, w2, w3), writing));
    }

}