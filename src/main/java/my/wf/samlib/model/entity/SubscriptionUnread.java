package my.wf.samlib.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

@Entity
@Table(name = "subscription_unread"
        , uniqueConstraints = {@UniqueConstraint(columnNames = {"subscription_id", "writing_id"})}
)
public class SubscriptionUnread  extends BaseEntity{
    private Subscription subscription;
    private Writing writing;


    @ManyToOne
    @JoinColumn(name = "subscription_id")
    @JsonBackReference
    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    @ManyToOne
    @JoinColumn(name = "writing_id")
    @JsonManagedReference
    public Writing getWriting() {
        return writing;
    }

    public void setWriting(Writing writing) {
        this.writing = writing;
    }
}
