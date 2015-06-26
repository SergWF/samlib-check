package my.wf.samlib.model.repositoriy;

import my.wf.samlib.model.entity.Subscription;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface SubscriptionRepository extends Repository<Subscription, Long> {

    Set<Subscription> findByCustomerId(@Param("customerId") Long customerId);
}
