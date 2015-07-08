package my.wf.samlib.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "subscription",
uniqueConstraints = {@UniqueConstraint(columnNames = {"customer_id", "author_id"})})
public class Subscription extends BaseEntity {
    private Customer customer;
    private Author author;
    private Date subscribedDate;
    private Set<SubscriptionUnread> subscriptionUnreads = new HashSet<>();


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
    @JsonManagedReference
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
                "id=" + getId() +
                ", customer=" + customer +
                ", author=" + author +
                ", subscribedDate=" + subscribedDate +
                '}';
    }
}
