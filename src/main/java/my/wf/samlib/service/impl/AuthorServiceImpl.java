package my.wf.samlib.service.impl;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.model.repositoriy.AuthorRepository;
import my.wf.samlib.model.repositoriy.WritingRepository;
import my.wf.samlib.service.AuthorService;
import my.wf.samlib.service.UtilsService;
import my.wf.samlib.tools.LinkTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {
    private static final Logger logger = LoggerFactory.getLogger(AuthorServiceImpl.class);


    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    WritingRepository writingRepository;
    @Autowired
    private UtilsService utilsService;
    private String linkSuffix;

    @Value("${link.suffix}")
    public void setLinkSuffix(String linkSuffix) {
        this.linkSuffix = linkSuffix;
    }

    @Override
    @Transactional
    public Author addAuthor(String url) {
        String authorUrl = LinkTool.getAuthorLink(url, linkSuffix);
        Author author = authorRepository.findByLink(authorUrl);
        return (null != author)
                ? author
                : createAndSaveNewAuthor(authorUrl);
    }

    protected Author createAndSaveNewAuthor(String url) {
        Author author;
        author = new Author();
        author.setName("author " + url);
        author.setLink(url);
        return authorRepository.save(author);
    }

    @Override
    public Author findAuthor(long authorId) {
        return authorRepository.findOne(authorId);
    }

    @Override
    public Author findAuthorWithWritings(long authorId) {
        return authorRepository.findOneWithWritings(authorId);
    }

    @Override
    public List<Author> findAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public void delete(long authorId) {
        authorRepository.delete(authorId);
    }

    @Override
    public Writing getWritingById(long writingId) {
        return writingRepository.findOne(writingId);
    }

    @Override
    public Author saveAuthor(Author author) {
        return authorRepository.save(author);
    }

}


