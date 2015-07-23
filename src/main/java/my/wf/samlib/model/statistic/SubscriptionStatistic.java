package my.wf.samlib.model.statistic;

import java.util.Date;

public class SubscriptionStatistic {
    private Long subscriptionId;
    private Long authorId;
    private String authorName;
    private String authorLink;
    private Long unreadCount;
    private Long writingCount;
    private Date lastUpdateDate;

    public SubscriptionStatistic(Object data) {
        Object[] objData = (Object[]) data;
        this.subscriptionId = (Long) objData[0];
        this.authorId = (Long) objData[1];
        this.authorName = (String) objData[2];
        this.authorLink = (String) objData[3];
        this.unreadCount = (Long) objData[4];
        this.writingCount = (Long) objData[5];
        this.lastUpdateDate = (Date) objData[6];
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

    public Long getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthorLink() {
        return authorLink;
    }
}
