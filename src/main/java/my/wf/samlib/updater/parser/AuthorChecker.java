package my.wf.samlib.updater.parser;

import my.wf.samlib.model.entity.Author;

import java.time.LocalDateTime;

public interface AuthorChecker {

    Author checkAuthorUpdates(Author author, LocalDateTime checkDate);

    boolean checkIpState();
}
