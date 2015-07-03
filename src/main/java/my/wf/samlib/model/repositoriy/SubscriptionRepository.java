package my.wf.samlib.model.repositoriy;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN 'true' ELSE 'false' END FROM Subscription s WHERE s.customer.id=:customerId AND s.id = :subscriptionId")
    Boolean exists(@Param("customerId") Long customerId, @Param("subscriptionId") Long subscriptionId);

    @Query("SELECT s, a FROM Subscription s  INNER JOIN FETCH s.author a WHERE s.customer.id = :customerId")
    Set<Subscription> findAllByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT s FROM Subscription s WHERE s.customer.id = :customerId AND s.author.id = :authorId ")
    Subscription findByCustomerAndAuthor(@Param("customerId") Long customerId, @Param("authorId") Long authorId);

    @Query("SELECT s,u FROM Subscription s LEFT JOIN FETCH s.subscriptionUnreads u WHERE s.author.id = :authorId ")
    Set<Subscription> findAllWithUnreadsByAndAuthorId(@Param("authorId") Long authorId);

    @Query("SELECT s,u FROM Subscription s LEFT JOIN FETCH s.subscriptionUnreads u WHERE s.customer.id = :customerId AND s.author.id = :authorId ")
    Subscription findByCustomerAndAuthorWithUnread(@Param("customerId") Long customerId, @Param("authorId") Long authorId);

    @Query("SELECT s, u FROM Subscription s INNER JOIN FETCH s.subscriptionUnreads u  WHERE s.customer.id = :customerId")
    Set<Subscription> findUnreadOnly(Long customerId);

    @Query("SELECT COUNT(s) FROM Subscription s  WHERE s.customer.id = :customerId")
    Integer subscriptionCountByCustomerID(@Param("customerId") Long customerId);

    @Query("SELECT MAX(w.lastChangedDate) FROM Subscription s  INNER JOIN s.author.writings w WHERE s.customer.id = :customerId")
    Date getLastChangedDateByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT s.id, COUNT(DISTINCT u), COUNT(DISTINCT w), MAX(w.lastChangedDate) FROM Subscription s LEFT JOIN s.subscriptionUnreads u INNER JOIN s.author a INNER JOIN a.writings w WHERE  s.id = :subscriptionId")
    Object getSubscriptionStatistic(@Param("subscriptionId") Long subscriptionId);

    @Query("SELECT s.id, COUNT(DISTINCT u), COUNT(DISTINCT w), MAX(w.lastChangedDate) FROM Subscription s LEFT JOIN s.subscriptionUnreads u INNER JOIN s.author a INNER JOIN a.writings w WHERE  s.customer.id = :customerId GROUP BY s.id")
    List<Object> getAllSubscriptionStatistic(@Param("customerId") Long customerId);
}
