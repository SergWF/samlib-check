package my.wf.samlib.model.statistic;

import java.util.Date;

public class SubscriptionStatistic {
    private Long subscriptionId;
    private Long unreadCount;
    private Long writingCount;
    private Date lastUpdateDate;

    public SubscriptionStatistic(Object data) {
        Object[] objData = (Object[]) data;
        this.subscriptionId = (Long) objData[0];
        this.unreadCount = (Long) objData[1];
        this.writingCount = (Long) objData[2];
        this.lastUpdateDate = (Date) objData[3];
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public Long getUnreadCount() {
        return unreadCount;
    }

    public Long getWritingCount() {
        return writingCount;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

}
