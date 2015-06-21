package my.wf.samlib.model.repositoriy;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Writing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    Author findByLink(String link);

    @Query("SELECT a FROM Customer c JOIN c.authors a WHERE c.id=:customerId")
    Set<Author> getListByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT w  FROM Customer c JOIN c.unreadWritings w WHERE c.id=:customerId")
    Set<Writing> getUnreadListByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT DISTINCT a.link FROM Author a")
    Set<String> findAllAuthorLinks();

    @Query("SELECT a, w FROM Author a LEFT JOIN FETCH a.writings w")
    Set<Author> findAllWithWritings();

    @Query("SELECT a, w FROM Author a LEFT JOIN FETCH a.writings w WHERE a.id=:authorId")
    Author findOneWithWritings(@Param("authorId") Long authorId);

    @Query("SELECT a, w FROM Author a LEFT JOIN FETCH a.writings w WHERE a.link=:link")
    Author findOneByLinkWithWritings(@Param("link") String link);

    @Query("SELECT a, w FROM Author a INNER JOIN FETCH a.writings w WHERE w.lastChangedDate >= :lastCheckDate")
    Set<Author> findUpdatedAuthors(@Param("lastCheckDate") Date lastCheckDate);

}
