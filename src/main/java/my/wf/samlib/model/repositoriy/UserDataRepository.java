package my.wf.samlib.model.repositoriy;

import my.wf.samlib.model.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, Long> {
    UserData findByUsername(String email);
}
