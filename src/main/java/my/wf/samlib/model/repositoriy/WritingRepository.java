package my.wf.samlib.model.repositoriy;

import my.wf.samlib.model.entity.Writing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WritingRepository extends JpaRepository<Writing, Long> {
}
