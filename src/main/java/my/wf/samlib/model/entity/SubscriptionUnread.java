package my.wf.samlib.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "subscription_unread")
public class SubscriptionUnread {
    private Long id;
    private Subscription subscription;
    private Writing writing;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    @ManyToOne
    @JoinColumn(name = "writing_id")
    public Writing getWriting() {
        return writing;
    }

    public void setWriting(Writing writing) {
        this.writing = writing;
    }
}
