package my.wf.samlib.updater.parser;

import my.wf.samlib.model.entity.Author;

public interface AuthorChecker {

    Author checkAuthorUpdates(Author author);

    boolean checkIpState();
}
