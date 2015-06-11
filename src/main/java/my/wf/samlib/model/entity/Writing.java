package my.wf.samlib.model.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "writing")
public class Writing extends BaseEntity {
    private String link;
    private Author author;
    private String description;
    private String groupName;
    private String size;
    private Date lastChangedDate;

    @Column(name="link")
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @ManyToOne
    @JoinColumn(name = "author_id")
    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "size")
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Column(name = "last_changed_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastChangedDate() {
        return lastChangedDate;
    }

    public void setLastChangedDate(Date lastChangedDate) {
        this.lastChangedDate = lastChangedDate;
    }

    @Column(name = "group_name")
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Transient
    public Boolean unreadByCustomer(Customer customer) {
        return customer.getUnreadWritings().contains(this);
    }
}
