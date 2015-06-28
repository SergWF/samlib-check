package my.wf.samlib.model.repositoriy;

import my.wf.samlib.model.entity.SubscriptionUnread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubscriptionUnreadRepository extends JpaRepository<SubscriptionUnread, Long> {
    SubscriptionUnread findBySubscriptionIdAndWritingId(Long subscriptionId, Long writingId);

    @Query("SELECT DISTINCT COUNT(u) FROM SubscriptionUnread u  WHERE u.subscription.customer.id = :customerId")
    Integer unreadCountByCustomerId(@Param("customerId") Long customerId);

}
