package my.wf.samlib.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "subscription_unread")
public class SubscriptionUnread  extends BaseEntity{
    private Subscription subscription;
    private Writing writing;


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
