package my.wf.samlib.updater.parser;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Writing;

import java.util.Date;
import java.util.Set;

public interface AuthorChangesChecker {
    Set<Writing> checkUpdatedWritings(Author newAuthor, Author oldAuthor, Date checkDate);
}
