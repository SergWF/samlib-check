package my.wf.samlib.updater.parser.impl;

import my.wf.samlib.helpers.EntityHelper;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.service.UtilsService;
import my.wf.samlib.updater.parser.SamlibPageReader;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Date;

public class AuthorCheckerTest {

    private static final String FULL_LINK = "http://1/index.html";
    private static final String SHORT_LINK = "http://1/";
    @InjectMocks
    @Spy
    AuthorCheckerImpl authorChecker;
    @Mock
    SamlibPageReader samlibPageReader;
    @Spy
    SamlibAuthorParserImpl samlibAuthorParser;

    @Mock
    UtilsService utilsService;
    Author author;
    Writing[] writings;



    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        authorChecker.setLinkSuffix("index.html");
        author = EntityHelper.createAuthor("http://1", "some author");
        author.setId(1L);
        writings = new Writing[]{
            EntityHelper.createWriting("w1", author, new Date(new Date().getTime() - 100000)),
            EntityHelper.createWriting("w2", author, new Date(new Date().getTime() - 110000)),
            EntityHelper.createWriting("w3", author, new Date(new Date().getTime() - 120000)),
            EntityHelper.createWriting("w4", author, new Date(new Date().getTime() - 100000)),
            EntityHelper.createWriting("w5", author, new Date(new Date().getTime() - 110000))
        };
    }

//
//    @Test
//    public void testImplementChanges() throws Exception {
//        Date date = new Date(new Date().getTime() - 11000);
//        Map<Writing, Writing> map = new HashMap<>();
//
//        Writing w0 = EntityHelper.makeCopy(writings[0]);
//        Writing w1 = EntityHelper.makeCopy(writings[1]);
//        Writing w2 = EntityHelper.makeCopy(writings[2]);
//        Writing w3 = EntityHelper.makeCopy(writings[3]);
//
//        w0.setLink("http://new_w0");
//        w0.setName("new W0");
//        w0.setSize("44");
//        w1.setSize("23");
//        w1.setDescription("QQQ");
//        w2.setDescription("QQQWWWEEE");
//        w3.setGroupName("EEE--WWW");
//
//
//        map.put(writings[0], null);
//        map.put(writings[1], w1);
//        map.put(writings[2], w2);
//        map.put(writings[3], w3);
//        map.put(writings[4], null);
//
//        Author changed = authorChecker.implementChanges(author, Arrays.asList(w0, w1, w2, w3), "new name", date);
//
//        Assert.assertThat(changed,
//                Matchers.allOf(
//                        Matchers.hasProperty("name", Matchers.equalTo("new name")),
//                        Matchers.hasProperty("lastChangedDate", Matchers.equalTo(date)),
//                        Matchers.hasProperty("writings",
//                                Matchers.containsInAnyOrder(w0, writings[1], writings[2], writings[3])
//                        )
//                )
//        );
//    }
//
//    @Test
//    public void testHandleOldWritings() throws Exception {
//        Date date = new Date(new Date().getTime() - 11000);
//        Map<Writing, Writing> map = new HashMap<>();
//
//        Writing w1 = EntityHelper.makeCopy(writings[1]);
//        w1.setSize("23");
//        w1.setDescription("QQQ");
//        Writing w2 = EntityHelper.makeCopy(writings[2]);
//        w2.setDescription("QQQWWWEEE");
//        Writing w3 = EntityHelper.makeCopy(writings[3]);
//        w3.setGroupName("EEE--WWW");
//
//        map.put(writings[0], null);
//        map.put(writings[1], w1);
//        map.put(writings[2], w2);
//        map.put(writings[3], w3);
//        map.put(writings[4], null);
//        Author handled = authorChecker.handleOldWritings(author, map, date);
//        Assert.assertThat(handled,
//                Matchers.allOf(
//                        Matchers.hasProperty("lastChangedDate", Matchers.equalTo(date)),
//                        Matchers.hasProperty("writings",
//                                Matchers.hasItems(
//                                        Matchers.allOf(
//                                                Matchers.hasProperty("size", Matchers.equalTo("23")),
//                                                Matchers.hasProperty("description", Matchers.equalTo("QQQ"))
//                                        ),
//                                        Matchers.hasProperty("description", Matchers.equalTo("QQQWWWEEE")),
//                                        Matchers.hasProperty("groupName", Matchers.equalTo("EEE--WWW"))
//                                )
//                        )
//                )
//        );
//    }
//
//    @Test
//    public void testHasChangesSize() throws Exception {
//        Writing w1 = EntityHelper.makeCopy(writings[1]);
//        w1.setSize("30");
//        Assert.assertTrue(authorChecker.hasChanges(w1, writings[1]));
//    }
//
//    @Test
//    public void testHasChangesDescription() throws Exception {
//        Writing w1 = EntityHelper.makeCopy(writings[1]);
//        w1.setDescription(UUID.randomUUID().toString());
//        Assert.assertTrue(authorChecker.hasChanges(w1, writings[1]));
//    }
//
//    @Test
//    public void testHasChangesGroupName() throws Exception {
//        Writing w1 = EntityHelper.makeCopy(writings[1]);
//        w1.setGroupName(UUID.randomUUID().toString());
//        Assert.assertTrue(authorChecker.hasChanges(w1, writings[1]));
//    }
//
//    @Test
//    public void testHasChangesNegative() throws Exception {
//        Writing w1 = EntityHelper.makeCopy(writings[1]);
//        Assert.assertFalse(authorChecker.hasChanges(w1, writings[1]));
//    }
//
//    @Test
//    public void testFindSame() throws Exception {
//        Writing w1 = EntityHelper.makeCopy(writings[1]);
//        Writing w2 = EntityHelper.makeCopy(writings[2]);
//        Writing w3 = EntityHelper.makeCopy(writings[3]);
//        Map<Writing, Writing> same = authorChecker.findSame(author.getWritings(), Arrays.asList(w1, w2, w3));
//        Assert.assertThat(same,
//                Matchers.allOf(
//                        Matchers.hasEntry(writings[0], null),
//                        Matchers.hasEntry(writings[1], w1),
//                        Matchers.hasEntry(writings[2], w2),
//                        Matchers.hasEntry(writings[3], w3),
//                        Matchers.hasEntry(writings[4], null)
//                )
//        );
//    }
//
//    @Test
//    public void testFindSameWritingByLink() throws Exception {
//        Writing writing = EntityHelper.makeCopy(writings[2]);
//        Assert.assertEquals(writings[2], authorChecker.findSameWriting(author.getWritings(), writing));
//    }
//
//    @Test
//    public void testFindSameWritingByLinkNegative() throws Exception {
//        Writing writing = new Writing();
//        writing.setLink("aaa");
//        Assert.assertNull(authorChecker.findSameWriting(author.getWritings(), writing));
//    }
//
//    @Test
//    public void testContinueCheckingIfOneWasFailed(){
////        Map<Integer, Author> authors = new HashMap<>();
////        int errorIndex = 2;
////        authors.put(0, EntityHelper.createAuthor("http://link1/", "one"));
////        authors.put(1, EntityHelper.createAuthor("http://link2/", "two"));
////        authors.put(2, EntityHelper.createAuthor("http://link3/", "error!"));
////        authors.put(3, EntityHelper.createAuthor("http://link4/", "four"));
////        authors.put(4, EntityHelper.createAuthor("http://link5/", "five"));
////        Mockito.doReturn(new HashSet<>(authors.values())).when(authorRepository).findAllWithWritings();
////        for(Author author: authors.values()){
////            Mockito.doReturn(author).when(authorChecker).checkAuthorUpdates(author);
////        }
////        Mockito.doThrow(RuntimeException.class).when(authorRepository).save(authors.get(errorIndex));
////        UpdatingProcessDto updatingProcessDto = authorChecker.doCheckUpdates();
////        Assert.assertThat(updatingProcessDto, Matchers.allOf(
////                        Matchers.hasProperty("processed", Matchers.equalTo(authors.size())),
////                        Matchers.hasProperty("errors", Matchers.equalTo(1)),
////                        Matchers.hasProperty("total", Matchers.equalTo(authors.size()))
////                )
////        );
//
//
//    }
//
//    private String loadPage(String path) throws IOException {
//        return IOUtils.toString(this.getClass().getResourceAsStream(path), Charset.forName("Cp1251"));
//    }

}