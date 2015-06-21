package my.wf.samlib.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import my.wf.samlib.model.compare.LastDate;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "writing")
public class Writing extends BaseEntity implements LastDate {
    private String link;
    private Author author;
    private String description;
    private String groupName;
    private String size;
    private String prevSize;
    private Date lastChangedDate;
    private Set<Customer> customers = new HashSet<>();

    @Column(name="link")
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonBackReference
    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Column(name = "description", length = 4096)
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
    @Override
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



    //@ManyToMany(mappedBy = "unreadWritings")
    @ManyToMany
    @JoinTable(name = "customer_unread_writing"
            , joinColumns = @JoinColumn(name = "writing_id")
            , inverseJoinColumns = @JoinColumn(name = "customer_id")
    )
    @JsonBackReference
    public Set<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }

    @Transient
    public Boolean unreadByCustomer(Customer customer) {
        return customer.getUnreadWritings().contains(this);
    }

    @Override
    public String toString() {
        return "Writing{" +
                "groupName='" + groupName + '\'' +
                ", name='" + getName() + '\'' +
                ", link='" + link + '\'' +
                ", author=" + (null == author? "": author.getName()) +
                ", description='" + description + '\'' +
                ", size='" + size + '\'' +
                ", prevSize='" + prevSize + '\'' +
                ", lastChangedDate=" + lastChangedDate +
                '}';
    }
}
