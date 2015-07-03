package my.wf.samlib.updater;

import my.wf.samlib.updater.parser.AuthorChecker;

public interface AuthorCheckerFactory {

    AuthorChecker getAuthorChecker();
}
