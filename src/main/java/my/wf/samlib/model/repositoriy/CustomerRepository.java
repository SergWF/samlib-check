package my.wf.samlib.model.repositoriy;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c, a, w, u FROM Customer c LEFT JOIN FETCH c.authors a LEFT JOIN FETCH a.writings w LEFT JOIN FETCH c.unreadWritings u WHERE c.id=:customerId")
    Customer getWithFullAuthorList(@Param("customerId") long customerId);
}
