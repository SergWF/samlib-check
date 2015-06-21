package my.wf.samlib.model.repositoriy;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
