package my.wf.samlib.model.entity;

import my.wf.samlib.helpers.EntityHelper;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class WritingTest {
    @Test
    public void equalsTest() throws Exception {
        Author author1 = EntityHelper.createAuthorWithId(1L, "http://1", "a1");
        Author author2 = EntityHelper.createAuthorWithId(2L, "http://2", "a2");
        Writing w0 = EntityHelper.createWriting("w1", author1);
        Writing w1 = EntityHelper.createWriting("w1", author1);
        Writing w2 = EntityHelper.createWriting("w2", author1);
        Writing w3 = EntityHelper.createWriting("w1", author2);

        Assert.assertTrue(w1.equals(w0));
        Assert.assertFalse(w1.equals(w2));
        Assert.assertFalse(w1.equals(w3));
    }

    @Test
    public void hashCodeTest() throws Exception {
        Author author1 = EntityHelper.createAuthorWithId(1L, "http://1", "a1");
        Author author2 = EntityHelper.createAuthorWithId(2L, "http://2", "a2");
        Writing w10 = EntityHelper.createWriting("w1", author1);
        Writing w11 = EntityHelper.createWriting("w1", author1);
        Writing w12 = EntityHelper.createWriting("w2", author1);
        Writing w21 = EntityHelper.createWriting("w1", author2);
        Assert.assertEquals(w11.hashCode(), w10.hashCode());
        Assert.assertFalse(w11.hashCode() == w12.hashCode());
        Assert.assertFalse(w11.hashCode() == w21.hashCode());
    }

}