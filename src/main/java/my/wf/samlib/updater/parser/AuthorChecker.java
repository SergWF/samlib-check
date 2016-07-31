package my.wf.samlib.updater.parser;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.updater.AuthorDelta;

import java.time.LocalDateTime;

public interface AuthorChecker {

    AuthorDelta checkAuthorUpdates(Author author, LocalDateTime checkDate);

    Author applyChanges(AuthorDelta delta);
}
