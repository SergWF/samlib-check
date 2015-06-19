package my.wf.samlib.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import my.wf.samlib.model.compare.LastChangedDateComparator;
import my.wf.samlib.model.compare.LastDate;

import javax.persistence.*;
import java.util.*;


@Entity
@Table(name = "author")
public class Author extends BaseEntity implements LastDate  {

    private Set<Writing> writings = new HashSet<Writing>();
    private String link;
    private Set<Customer> subscribers = new HashSet<>();

    @Column(name="link", unique = true)
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    public Set<Writing> getWritings() {
        return writings;
    }

    public void setWritings(Set<Writing> writings) {
        this.writings = writings;
    }

    @ManyToMany//(mappedBy = "authors")
    @JoinTable(name = "customer_author"
            , joinColumns = @JoinColumn(name = "author_id")
            , inverseJoinColumns = @JoinColumn(name = "customer_id")
    )
    @JsonBackReference
    public Set<Customer> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Set<Customer> subscribers) {
        this.subscribers = subscribers;
    }

    @Transient
    @Override
    public Date getLastChangedDate() {
        if(writings.isEmpty()){
            return null;
        }
        return Collections.max(writings, new LastChangedDateComparator()).getLastChangedDate();
    }

    @Transient
    public Boolean unreadByCustomer(Customer customer) {
        for (Writing writing : customer.getUnreadWritings()) {
            if (writing.getAuthor().equals(this)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Author{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", link='" + link + '\'' +
                ", writings=[" +
                getWritings().toString() +
                "]"+
                '}';
    }
}
