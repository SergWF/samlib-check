package my.wf.samlib.service;

import my.wf.samlib.model.entity.Author;

import java.util.Collection;
import java.util.List;

public interface AuthorService {

    Author addAuthor(String authorUrl);

    Author findAuthor(long authorId);

    Integer importAuthors(Collection<String> authorLinks);

    List<String> exportAuthors();

    void delete(long authorId);
}
