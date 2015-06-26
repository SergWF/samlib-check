package my.wf.samlib.model.repositoriy;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.model.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.ColumnResult;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.SqlResultSetMapping;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c, a, w, u FROM Customer c LEFT JOIN FETCH c.authors a LEFT JOIN FETCH a.writings w LEFT JOIN FETCH c.unreadWritings u WHERE c.id=:customerId")
    Customer findOneWithFullData(@Param("customerId") long customerId);

    @Query("SELECT COUNT(a) FROM Customer c INNER JOIN c.authors a WHERE c.id = :customerId")
    Integer subscriptionCount(@Param("customerId") Long customerId);

    @Query("SELECT DISTINCT COUNT(u.author) FROM Customer c INNER JOIN c.unreadWritings u  WHERE c.id = :customerId")
    Integer notReadCount(@Param("customerId") Long customerId);

    @Query("SELECT MAX(w.lastChangedDate) FROM Customer c INNER JOIN c.authors a INNER JOIN a.writings w WHERE c.id = :customerId")
    Date getLastChangedDate(@Param("customerId") Long customerId);

    @Query("SELECT a, w  FROM Author a INNER JOIN FETCH a.writings w INNER JOIN a.subscribers c INNER JOIN c.unreadWritings u WHERE u=w AND c.id = :customerId")
    Set<Author> findUnreadAuthors(@Param("customerId") Long customerId);

    @Query(nativeQuery = true, value = " SELECT " +
            " a.ID, a.NAME, a.LINK, count(w.ID) as writingsCount,count(u.WRITING_ID) as unreadCount, max(w.LAST_CHANGED_DATE) as lastChanged\n" +
            " FROM CUSTOMER_AUTHOR ca\n" +
            " INNER JOIN AUTHOR a ON ca.AUTHOR_ID = a.ID\n" +
            " INNER JOIN WRITING w ON w.AUTHOR_ID = ca.AUTHOR_ID\n" +
            " LEFT JOIN CUSTOMER_UNREAD_WRITING u ON w.ID = WRITING_ID AND u.CUSTOMER_ID = ca.CUSTOMER_ID\n" +
            " WHERE ca.CUSTOMER_ID = :customerId\n" +
            " GROUP BY a.ID, a.NAME, a.LINK")
    Set<Subscription> getSubscriptionList(@Param("customerId") Long customerId);

}
