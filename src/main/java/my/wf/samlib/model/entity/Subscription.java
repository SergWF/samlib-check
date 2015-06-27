package my.wf.samlib.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "subscription")
public class Subscription {
    private Long id;
    private Customer customer;
    private Author author;
    private Date subscribedDate;
    private Set<SubscriptionUnread> subscriptionUnreads = new HashSet<>();

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonBackReference
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    @Column(name = "subscription_date")
    @Temporal(TemporalType.DATE)
    public Date getSubscribedDate() {
        return subscribedDate;
    }

    public void setSubscribedDate(Date subscribedDate) {
        this.subscribedDate = subscribedDate;
    }

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    public Set<SubscriptionUnread> getSubscriptionUnreads() {
        return subscriptionUnreads;
    }

    public void setSubscriptionUnreads(Set<SubscriptionUnread> subscriptionUnreads) {
        this.subscriptionUnreads = subscriptionUnreads;
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + id +
                ", customer=" + customer +
                ", author=" + author +
                ", subscribedDate=" + subscribedDate +
                ", subscriptionUnreads=" + subscriptionUnreads +
                '}';
    }
}
