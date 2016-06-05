package my.wf.samlib.updater.parser.impl;

import my.wf.samlib.helpers.EntityHelper;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Writing;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.*;

public class AuthorCheckerImplTest {

    private AuthorCheckerImpl authorChecker;
    private Author author;

    @Before
    public void setUp() throws Exception {
        authorChecker = new AuthorCheckerImpl();
        author = EntityHelper.createAuthor("a", "a");

    }

    @Test
    public void checkAuthorUpdates() throws Exception {

    }

    @Test
    public void checkIpState() throws Exception {

    }

    @Test
    public void implementChanges() throws Exception {

    }

    @Test
    public void checkChanges() throws Exception {
        Writing w1 = EntityHelper.createWriting("w1", author);
        Writing w2 = EntityHelper.createWriting("w2", author);
        Writing w3 = EntityHelper.createWriting("w3", author);
        Writing writing = EntityHelper.createWriting("w2", author);
        Date checkDate = new Date(2011, 11, 15, 10, 0, 0);
        authorChecker.checkChanges(writing, Arrays.asList(w1, w2, w3), checkDate);
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