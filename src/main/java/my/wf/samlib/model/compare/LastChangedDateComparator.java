package my.wf.samlib.model.compare;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;

public class LastChangedDateComparator implements Comparator<LastDate> {

    @Override
    public int compare(LastDate o1, LastDate o2) {
        LocalDateTime d1 = (null == o1)?null:o1.getLastChangedDate();
        LocalDateTime  d2 = (null == o2)?null:o2.getLastChangedDate();
        return  (null == d1) ?
                (null == d2 ? 0 : -1) :
                (null == d2 ? 1 : d1.compareTo(d2));
    }
}
