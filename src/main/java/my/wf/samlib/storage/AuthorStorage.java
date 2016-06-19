package my.wf.samlib.storage;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Writing;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

public interface AuthorStorage {
    Author addAuthor(Author author) throws IOException;
    Author save(Author author);
    void delete(long authorId);
    void delete(Author author);
    Author findByLink(String authorUrl);
    Author getById(long id);
    Set<Author> getAll();
    Writing findWritingByLink(String authorLink, String writingLink);
    Set<Author> getUpdatedAfter(LocalDateTime date);
    long getCount();

    LocalDateTime getLastUpdateDate();

    void flushIfRequired() throws IOException;

    void loadFromPhysicalStorage() throws IOException;

    void setLastUpdateDate(LocalDateTime lastUpdateDate);

    void save(Writing writing) throws IOException;
}
