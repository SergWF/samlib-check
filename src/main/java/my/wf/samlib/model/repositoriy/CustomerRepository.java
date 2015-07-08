package my.wf.samlib.model.repositoriy;

import my.wf.samlib.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c, s, a, u " +
            " FROM Customer c " +
            " LEFT JOIN FETCH c.subscriptions s " +
            " LEFT JOIN FETCH s.author a " +
            " LEFT JOIN FETCH s.subscriptionUnreads u " +
            "WHERE c.id = :customerId")
    Customer findOneWithSubscriptions(@Param("customerId")long customerId);

    @Query("SELECT c, s, a, u " +
            " FROM Customer c " +
            " LEFT JOIN FETCH c.subscriptions s " +
            " LEFT JOIN FETCH s.author a " +
            " LEFT JOIN FETCH s.subscriptionUnreads u ")
    Set<Customer> findAllWithSubscriptions();

}
