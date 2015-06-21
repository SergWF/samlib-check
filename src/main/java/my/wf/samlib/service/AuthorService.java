package my.wf.samlib.service;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Customer;

import java.util.Collection;
import java.util.Set;

public interface AuthorService {

    Author addAuthor(String authorUrl);

    Author findAuthor(long authorId);

    Integer importAuthors(Customer customer, Collection<String> authorLinks);

    Set<String> exportAuthors();

    void delete(long authorId);
}
