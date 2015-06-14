package my.wf.samlib.model.repositoriy;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.model.entity.Writing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("SELECT a FROM Customer c JOIN c.authors a WHERE c.id=:customerId")
    List<Author> getListByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT w  FROM Customer c JOIN c.unreadWritings w WHERE c.id=:customerId")
    List<Writing> getUnreadListByCustomerId(@Param("customerId") Long customerId);

    Author findByLink(String link);
}
