package my.wf.samlib.updater.parser;

import my.wf.samlib.model.entity.Author;

public interface AuthorCheckCallback {
    void onAuthorCheck(Author checkedAuthor);
}
