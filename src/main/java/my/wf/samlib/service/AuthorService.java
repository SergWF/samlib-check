package my.wf.samlib.service;

import my.wf.samlib.model.entity.Author;

public interface AuthorService {

    Author addAuthor(String authorUrl);

    Author findAuthor(long authorId);
}
