package my.wf.samlib.service;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Writing;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

public interface AuthorService {

    Author addAuthor(String authorUrl) throws IOException;

    Author findAuthor(long authorId);

    Set<Author> findAllAuthors();
    void delete(long authorId) throws IOException;

    Author saveAuthor(Author updated) throws IOException;

    Writing findWritingByLink(String authorLink, String writingLink);

    Set<Author> getUnreadAuthors();

    void markAllWritingsRead(Author author) throws IOException;

    void markWritingUnread(Writing writing) throws IOException;

    void markWritingRead(Writing writing) throws IOException;

    long getAllAuthorsCount();

    LocalDateTime getLastUpdateDate();

    void  setLastUpdateDate(LocalDateTime lastUpdateDate);

}
