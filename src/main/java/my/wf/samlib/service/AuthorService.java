package my.wf.samlib.service;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Writing;

import java.util.List;

public interface AuthorService {

    Author addAuthor(String authorUrl);

    Author findAuthor(long authorId);

    Author findAuthorWithWritings(long authorId);

    List<Author> findAllAuthors();


    void delete(long authorId);

    Writing getWritingById(long writingId);

    Author saveAuthor(Author updated);
}
