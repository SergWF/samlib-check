package my.wf.samlib.model.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@SqlResultSetMapping(name = "SubscriptionMapping",
        entities = {@EntityResult(
                entityClass = Subscription.class,
                fields = {
                        @FieldResult(name = "id", column = "id"),
                        @FieldResult(name = "name", column = "name"),
                        @FieldResult(name = "link", column = "link"),
                        @FieldResult(name = "writingsCount", column = "writingsCount"),
                        @FieldResult(name = "unreadCount", column = "unreadCount"),
                        @FieldResult(name = "lastChangedDate", column = "lastChanged")
                }
        )}
)
@NamedNativeQueries({
        @NamedNativeQuery(name = "Subscription.findByCustomerId", query = " SELECT " +
                " a.ID, a.NAME, a.LINK, count(w.ID) as writingsCount,count(u.WRITING_ID) as unreadCount, max(w.LAST_CHANGED_DATE) as lastChanged\n" +
                " FROM CUSTOMER_AUTHOR ca\n" +
                " INNER JOIN AUTHOR a ON ca.AUTHOR_ID = a.ID\n" +
                " INNER JOIN WRITING w ON w.AUTHOR_ID = ca.AUTHOR_ID\n" +
                " LEFT JOIN CUSTOMER_UNREAD_WRITING u ON w.ID = WRITING_ID AND u.CUSTOMER_ID = ca.CUSTOMER_ID\n" +
                " WHERE ca.CUSTOMER_ID = :customerId\n" +
                " GROUP BY a.ID, a.NAME, a.LINK", resultSetMapping = "SubscriptionMapping")
})
public class Subscription extends BaseEntity {
    private String link;
    private Integer writingsCount;
    private Integer unreadCount;
    private Date lastChangedDate;

    public Subscription() {
    }

    public Subscription(Long id, String name, String link, Integer writingsCount, Integer unreadCount, Date lastChangedDate) {
        setId(id);
        setName(name);
        this.link = link;
        this.writingsCount = writingsCount;
        this.unreadCount = unreadCount;
        this.lastChangedDate = lastChangedDate;
    }

    @Column(name = "link")
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Column(name = "writingsCount")
    public Integer getWritingsCount() {
        return writingsCount;
    }

    public void setWritingsCount(Integer writingsCount) {
        this.writingsCount = writingsCount;
    }

    @Column(name = "unreadCount")
    public Integer getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(Integer unreadCount) {
        this.unreadCount = unreadCount;
    }

    public Date getLastChangedDate() {
        return lastChangedDate;
    }

    @Column(name = "lastChanged")
    @Temporal(TemporalType.TIMESTAMP)
    public void setLastChangedDate(Date lastChangedDate) {
        this.lastChangedDate = lastChangedDate;
    }

    @Override
    public String toString() {
        return "Subscription{" +
                " id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", link='" + link + '\'' +
                ", writingsCount=" + writingsCount +
                ", unreadCount=" + unreadCount +
                ", lastChangedDate=" + lastChangedDate +
                '}';
    }
}
