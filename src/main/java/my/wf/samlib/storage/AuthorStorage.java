package my.wf.samlib.storage;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Writing;

import java.io.IOException;
import java.util.Date;
import java.util.Set;

public interface AuthorStorage {
    Author addAuthor(Author author) throws IOException;
    Author save(Author author) throws IOException;
    void delete(Author author) throws IOException;
    Author findByLink(String authorUrl);
    Author getById(long id);
    Set<Author> getAll();
    Writing findWritingByLink(String authorLink, String writingLink);
    Set<Author> getUpdatedAfter(Date date);
    long getCount();
}
