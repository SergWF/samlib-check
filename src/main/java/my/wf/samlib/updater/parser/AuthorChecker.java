package my.wf.samlib.updater.parser;

import my.wf.samlib.model.entity.Author;

public interface AuthorChecker {
    //void checkAuthors(Collection<Author> authors, Date checkDate, AuthorCheckCallback callback);

    Author checkAuthorUpdates(Author author);
}
