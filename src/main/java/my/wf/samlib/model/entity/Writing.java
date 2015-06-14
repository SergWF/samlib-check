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
    private String prevSize;
    private Date lastChangedDate;
    private Date samlibDate;

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

    @Column(name = "prev_size")
    public String getPrevSize() {
        return prevSize;
    }

    public void setPrevSize(String prevSize) {
        this.prevSize = prevSize;
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

    @Column(name = "samlib_date")
    @Temporal(TemporalType.DATE)
    public Date getSamlibDate() {
        return samlibDate;
    }

    public void setSamlibDate(Date samlibDate) {
        this.samlibDate = samlibDate;
    }

    @Transient
    public Boolean unreadByCustomer(Customer customer) {
        return customer.getUnreadWritings().contains(this);
    }
}
