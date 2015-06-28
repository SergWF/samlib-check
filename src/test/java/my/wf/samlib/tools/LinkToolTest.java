package my.wf.samlib.tools;

import my.wf.samlib.helpers.EntityHelper;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Writing;
import org.junit.Assert;
import org.junit.Test;

public class LinkToolTest {

    private static final String AUTHOR_URL = "http://samlib.ru/d/demchenko_aw/";
    private static final String AUTHOR_URL_NO_SLASH = "http://samlib.ru/d/demchenko_aw";
    private static final String FULL_AUTHOR_URL ="http://samlib.ru/d/demchenko_aw/indextitle.shtml";
    private static final String LINK_SUFFIX ="indextitle.shtml";
    private static final String WRITING_URL = "http://samlib.ru/d/demchenko_aw/page1.shtml";


    @Test
    public void testGetAuthorLink() {
        Assert.assertEquals(AUTHOR_URL, LinkTool.getAuthorLink(AUTHOR_URL, LINK_SUFFIX));
        Assert.assertEquals(AUTHOR_URL, LinkTool.getAuthorLink(AUTHOR_URL_NO_SLASH, LINK_SUFFIX));
        Assert.assertEquals(AUTHOR_URL, LinkTool.getAuthorLink(FULL_AUTHOR_URL, LINK_SUFFIX));
    }

    @Test
    public void testGetFullAuthorLink() {
        Assert.assertEquals(FULL_AUTHOR_URL, LinkTool.getFullAuthorLink(AUTHOR_URL, LINK_SUFFIX));
        Assert.assertEquals(FULL_AUTHOR_URL, LinkTool.getFullAuthorLink(AUTHOR_URL_NO_SLASH, LINK_SUFFIX));
        Assert.assertEquals(FULL_AUTHOR_URL, LinkTool.getFullAuthorLink(FULL_AUTHOR_URL, LINK_SUFFIX));
    }

    @Test
    public void testGetFullWritingLink(){
        Author author = EntityHelper.createAuthor(AUTHOR_URL, "some name");
        Writing writing = new Writing();
        writing.setLink("page1.shtml");
        writing.setAuthor(author);
        Assert.assertEquals(WRITING_URL, LinkTool.getFullWritingLink(writing));
    }
}