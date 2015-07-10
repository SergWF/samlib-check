package my.wf.samlib.updater.parser.impl;

import my.wf.samlib.helpers.EntityHelper;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.updater.parser.SamlibAuthorParser;
import my.wf.samlib.updater.parser.SamlibPageReader;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AuthorCheckerImplTest {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    private static  final String AUTHOR_LINK = "http://some link/";
    private static  final String AUTHOR_NAME = "some name";
    private static  final String AUTHOR_NOT_PARSED_PAGE_SRING = "some not parsed String";

    AuthorCheckerImpl authorChecker;

    //Author author;
    Writing ow0;
    Writing ow1;
    Writing ow2;
    Writing ow3;
    Writing ow4;

    Writing nw1;
    Writing nw2;
    Writing nw3;
    Writing nw4;
    List<Writing> oldWritings;
    List<Writing> newWritings;
    Map<Writing, Writing> map = new HashMap<>();
    Date date;
    SamlibAuthorParser samlibAuthorParser;
    SamlibPageReader samlibPageReader;

    @Before
    public void setUp() throws Exception {
        date = new Date(new Date().getTime() - 11000);
        authorChecker = Mockito.spy(new AuthorCheckerImpl());
        samlibAuthorParser = Mockito.mock(SamlibAuthorParser.class);
        samlibPageReader = Mockito.mock(SamlibPageReader.class);
        authorChecker.setSamlibAuthorParser(samlibAuthorParser);
        authorChecker.setSamlibPageReader(samlibPageReader);

        Author author = EntityHelper.createAuthor(UUID.randomUUID().toString(), "a1");
        ow0 = EntityHelper.createWriting("w0", author);
        ow1 = EntityHelper.createWriting("w1", author);
        ow2 = EntityHelper.createWriting("w2", author);
        ow3 = EntityHelper.createWriting("w3", author);
        ow4 = EntityHelper.createWriting("w4", author);

        nw1 = EntityHelper.makeCopy(ow1);
        nw2 = EntityHelper.makeCopy(ow2);
        nw3 = EntityHelper.makeCopy(ow3);
        nw4 = EntityHelper.createWriting("nw4", author);
        oldWritings = Arrays.asList(ow0, ow1, ow2, ow3, ow4);
        newWritings = Arrays.asList(nw1, nw2, nw3/*, nw4*/);
        long id = 100L;
        for (Writing writing :oldWritings) {
            writing.setId(id++);
        }

        for (Writing writing :newWritings) {
            writing.setLastChangedDate(date);
        }

        map.put(ow0, null);
        map.put(ow1, nw1);
        map.put(ow2, nw2);
        map.put(ow3, nw3);
        map.put(ow4, null);

        Mockito.doReturn(AUTHOR_NOT_PARSED_PAGE_SRING).when(samlibPageReader).readPage(Mockito.anyString());
        Mockito.doReturn(AUTHOR_NAME).when(samlibAuthorParser).parseAuthorName(Mockito.anyString());
        Mockito.doReturn(new HashSet<>(newWritings)).when(samlibAuthorParser).parseWritings(Mockito.anyString());

    }

    @Test
    public void testFindUnreadCount() throws ParseException {
        Author author = EntityHelper.createAuthor("http://a1", "a1");
        Writing w1 = EntityHelper.createWriting("w1", author, SIMPLE_DATE_FORMAT.parse("2015.01.01 12:30:00"));
        Writing w2 = EntityHelper.createWriting("w1", author, SIMPLE_DATE_FORMAT.parse("2015.01.01 12:30:00"));
        Writing w3 = EntityHelper.createWriting("w1", author, SIMPLE_DATE_FORMAT.parse("2015.01.01 12:30:00"));
        Writing w4 = EntityHelper.createWriting("w1", author, SIMPLE_DATE_FORMAT.parse("2015.01.01 12:35:00"));
        Writing w5 = EntityHelper.createWriting("w1", author, SIMPLE_DATE_FORMAT.parse("2015.01.01 12:40:00"));
        Writing w6 = EntityHelper.createWriting("w1", author, SIMPLE_DATE_FORMAT.parse("2015.01.01 12:40:00"));

        Assert.assertEquals(3, authorChecker.findUpdatedCount(author.getWritings(), SIMPLE_DATE_FORMAT.parse("2015.01.01 12:35:00")));
    }

    @Test
    public void testCheckAuthorUpdatesNewAuthor() {
        Author newAuthor = new Author();
        newAuthor.setLink(AUTHOR_LINK);
        Author updatedAuthor = authorChecker.checkAuthorUpdates(newAuthor);
        Assert.assertEquals(updatedAuthor, newAuthor);
        Assert.assertThat(newAuthor, Matchers.allOf(
                Matchers.hasProperty("link", Matchers.equalTo(AUTHOR_LINK)),
                Matchers.hasProperty("name", Matchers.equalTo(AUTHOR_NAME)),
                Matchers.hasProperty("writings", Matchers.hasSize(newWritings.size())),
                Matchers.hasProperty("writings", Matchers.hasItems(
                                Matchers.allOf(
                                        Matchers.hasProperty("name", Matchers.equalTo(nw1.getName())),
                                        Matchers.hasProperty("link", Matchers.equalTo(nw1.getLink()))
                                ),
                                Matchers.allOf(
                                        Matchers.hasProperty("name", Matchers.equalTo(nw2.getName())),
                                        Matchers.hasProperty("link", Matchers.equalTo(nw2.getLink()))
                                ),
                                Matchers.allOf(
                                        Matchers.hasProperty("name", Matchers.equalTo(nw3.getName())),
                                        Matchers.hasProperty("link", Matchers.equalTo(nw3.getLink()))
                                )
                        )
                )
        ));
    }


    @Test
    public void testCheckAuthorUpdatesNoChanged() {
        Author author = new Author();
        author.setLink(AUTHOR_LINK);
        author.setName(AUTHOR_NAME);
        author.getWritings().addAll(Arrays.asList(ow1, ow2, ow3));

        Author updatedAuthor = authorChecker.checkAuthorUpdates(author);
        Assert.assertEquals(updatedAuthor, author);

        Assert.assertThat(author, Matchers.allOf(
                Matchers.hasProperty("link", Matchers.equalTo(AUTHOR_LINK)),
                Matchers.hasProperty("name", Matchers.equalTo(AUTHOR_NAME)),
                Matchers.hasProperty("writings", Matchers.hasSize(3)),
                Matchers.hasProperty("writings", Matchers.hasItems(
                                Matchers.allOf(
                                        Matchers.hasProperty("name", Matchers.equalTo(ow1.getName())),
                                        Matchers.hasProperty("link", Matchers.equalTo(ow1.getLink()))
                                ),
                                Matchers.allOf(
                                        Matchers.hasProperty("name", Matchers.equalTo(ow2.getName())),
                                        Matchers.hasProperty("link", Matchers.equalTo(ow2.getLink()))
                                ),
                                Matchers.allOf(
                                        Matchers.hasProperty("name", Matchers.equalTo(ow3.getName())),
                                        Matchers.hasProperty("link", Matchers.equalTo(ow3.getLink()))
                                )
                        )
                )
        ));
    }


    @Test
    public void testImplementChanges() {
        Author author = new Author();
        author.setName("old name");
        author.getWritings().addAll(Arrays.asList(ow0, ow1,ow2,ow3,ow4));
        Writing nw0 = EntityHelper.makeCopy(ow0);

        nw0.setLink("http://new_w0");
        nw0.setName("new W0");
        nw0.setSize("44");
        nw1.setSize("23");
        nw1.setDescription("QQQ");
        nw2.setDescription("QQQWWWEEE");
        nw3.setGroupName("EEE--WWW");


        Author changed = authorChecker.implementChanges(author, Arrays.asList(nw0, nw1, nw2, nw3), "new name", date);

        Assert.assertThat(changed,
                Matchers.allOf(
                        Matchers.hasProperty("name", Matchers.equalTo("new name")),
                        Matchers.hasProperty("lastChangedDate", Matchers.equalTo(date)),
                        Matchers.hasProperty("writings", Matchers.hasSize(4)),
                        Matchers.hasProperty("writings",
                                Matchers.containsInAnyOrder(nw0, ow1, ow2, ow3)
                        )
                )
        );
    }


    @Test
    public void testHandleOldWritings() {
        Author author = new Author();
        author.getWritings().addAll(oldWritings);

        nw1.setSize("23");
        nw1.setDescription("QQQ");
        nw2.setDescription("QQQWWWEEE");
        nw3.setGroupName("EEE--WWW");

        Author handled = authorChecker.handleOldWritings(author, map, date);
        Assert.assertThat(handled,
                Matchers.allOf(
                        Matchers.hasProperty("lastChangedDate", Matchers.equalTo(date)),
                        Matchers.hasProperty("writings",
                                Matchers.hasItems(
                                        Matchers.allOf(
                                                Matchers.hasProperty("size", Matchers.equalTo("23")),
                                                Matchers.hasProperty("description", Matchers.equalTo("QQQ"))
                                        ),
                                        Matchers.hasProperty("description", Matchers.equalTo("QQQWWWEEE")),
                                        Matchers.hasProperty("groupName", Matchers.equalTo("EEE--WWW"))
                                )
                        )
                )
        );
    }

    @Test
    public void testHasChangesSize() throws Exception {
        Writing w1 = EntityHelper.makeCopy(ow1);
        w1.setSize("30");
        Assert.assertTrue(authorChecker.hasChanges(w1, ow1));
    }

    @Test
    public void testHasChangesDescription() throws Exception {
        Writing w1 = EntityHelper.makeCopy(ow1);
        w1.setDescription(UUID.randomUUID().toString());
        Assert.assertTrue(authorChecker.hasChanges(w1, ow1));
    }

    @Test
    public void testHasChangesGroupName() throws Exception {
        Writing w1 = EntityHelper.makeCopy(ow1);
        w1.setGroupName(UUID.randomUUID().toString());
        Assert.assertTrue(authorChecker.hasChanges(w1, ow1));
    }

    @Test
    public void testHasChangesNegative() throws Exception {
        Writing w1 = EntityHelper.makeCopy(ow1);
        Assert.assertFalse(authorChecker.hasChanges(w1, ow1));
    }

    @Test
    public void testIsSame() throws Exception {
        Assert.assertTrue(authorChecker.isSame(new Integer(10), new Integer(10)));
        Assert.assertTrue(authorChecker.isSame("some string", "some string"));
        Assert.assertTrue(authorChecker.isSame(null, null));
    }

    @Test
    public void testIsSameNegative() throws Exception {
        Assert.assertFalse(authorChecker.isSame(new Integer(10), new Integer(12)));
        Assert.assertFalse(authorChecker.isSame("some string", "another string"));
        Assert.assertFalse(authorChecker.isSame("some string", null));
        Assert.assertFalse(authorChecker.isSame(null, "some string"));
    }

    @Test
    public void testFindSame() throws Exception {
        Map<Writing, Writing> same = authorChecker.findSame(oldWritings, newWritings);
        Assert.assertThat(same,
                Matchers.allOf(
                        Matchers.hasEntry(ow0, null),
                        Matchers.hasEntry(ow1, nw1),
                        Matchers.hasEntry(ow2, nw2),
                        Matchers.hasEntry(ow3, nw3),
                        Matchers.hasEntry(ow4, null)
                )
        );
    }

    @Test
    public void testFindSameWritingByLink() throws Exception {
        Assert.assertEquals(ow2, authorChecker.findSameWriting(oldWritings, nw2));
    }

    @Test
    public void testFindSameWritingByLinkNegative() throws Exception {
        Writing writing = new Writing();
        writing.setLink("aaa");
        Assert.assertNull(authorChecker.findSameWriting(oldWritings, writing));
    }
}