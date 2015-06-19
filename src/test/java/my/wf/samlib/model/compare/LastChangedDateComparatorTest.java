package my.wf.samlib.model.compare;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class LastChangedDateComparatorTest {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    @Mock
    LastDate lastDate0;
    @Mock
    LastDate lastDate1;
    @Mock
    LastDate lastDate2;



    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Date nullDate = null;
        Mockito.doReturn(nullDate).when(lastDate1).getLastChangedDate();
        Mockito.doReturn(sdf.parse("2015.03.20 14:33:00")).when(lastDate1).getLastChangedDate();
        Mockito.doReturn(sdf.parse("2015.03.20 10:00:00")).when(lastDate2).getLastChangedDate();
    }

    @Test
    public void testCompare() throws Exception {
        List<LastDate> list = Arrays.asList(lastDate0, lastDate1, lastDate2);
        Collections.sort(list, new LastChangedDateComparator());
        Assert.assertEquals(lastDate0, list.get(0));
        Assert.assertEquals(lastDate2, list.get(1));
        Assert.assertEquals(lastDate1, list.get(2));
    }
    @Test
    public void testMax() throws Exception {
        List<LastDate> list = Arrays.asList(lastDate0, lastDate1, lastDate2);
        Assert.assertEquals(lastDate1, Collections.max(list, new LastChangedDateComparator()));
    }

    @Test
    public void testMin() throws Exception {
        List<LastDate> list = Arrays.asList(lastDate0, lastDate1, lastDate2);
        Assert.assertEquals(lastDate0, Collections.min(list, new LastChangedDateComparator()));
    }

}