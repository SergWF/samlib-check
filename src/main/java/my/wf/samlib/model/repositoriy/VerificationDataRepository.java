package my.wf.samlib.model.repositoriy;

import my.wf.samlib.model.entity.VerificationData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationDataRepository extends JpaRepository<VerificationData, String> {
}
