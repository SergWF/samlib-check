package my.wf.samlib.service.impl;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.service.AuthorService;
import my.wf.samlib.storage.AuthorStorage;
import my.wf.samlib.tools.LinkTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {
    private static final Logger logger = LoggerFactory.getLogger(AuthorServiceImpl.class);

    @Autowired
    private AuthorStorage authorStorage;

    private String linkSuffix;

    @Value("${link.suffix}")
    public void setLinkSuffix(String linkSuffix) {
        this.linkSuffix = linkSuffix;
    }

    @Override
    public Author addAuthor(String url) throws IOException {
        String authorUrl = LinkTool.getAuthorLink(url, linkSuffix);
        Author author = authorStorage.findByLink(authorUrl);
        return (null != author)
                ? author
                : createAndSaveNewAuthor(authorUrl);
    }

    Author createAndSaveNewAuthor(String url) throws IOException {
        Author author = new Author();
        author.setName("author " + url);
        author.setLink(url);
        author = authorStorage.save(author);
        logger.info("New Author was added by link [{}] and got an id={}", author.getLink(), author.getId());
        return author;
    }

    @Override
    public Author findAuthor(long authorId) {
        return authorStorage.getById(authorId);
    }

    @Override
    public Set<Author> findAllAuthors() {
        return authorStorage.getAll();
    }

    @Override
    public void delete(long authorId) throws IOException {
        authorStorage.delete(authorId);
    }


    @Override
    public Author saveAuthor(Author author) throws IOException {
        return authorStorage.save(author);
    }

    @Override
    public Writing findWritingByLink(String authorLink, String writingLink) {
        return authorStorage.findWritingByLink(LinkTool.getAuthorLink(authorLink, linkSuffix), writingLink);
    }

    @Override
    public Set<Author> getUnreadAuthors() {
        return authorStorage.getAll().stream().filter((a)->a.getUnread() > 0).collect(Collectors.toSet());
    }

    @Override
    public void markAllWritingsRead(Author author) throws IOException {
        author.getWritings().stream().forEach((w)->w.setUnread(false));
        authorStorage.save(author);
    }

    @Override
    public void markWritingUnread(Writing writing) throws IOException {
        writing.setUnread(true);
        authorStorage.save(writing.getAuthor());
    }

    @Override
    public void markWritingRead(Writing writing) throws IOException {
        writing.setUnread(false);
        authorStorage.save(writing.getAuthor());
    }

    @Override
    public long getAllAuthorsCount() {
        return authorStorage.getCount();
    }

    @Override
    public LocalDateTime getLastUpdateDate() {
        return authorStorage.getLastUpdateDate();
    }

}


