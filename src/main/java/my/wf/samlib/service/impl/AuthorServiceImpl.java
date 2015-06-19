package my.wf.samlib.service.impl;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.repositoriy.AuthorRepository;
import my.wf.samlib.model.repositoriy.CustomerRepository;
import my.wf.samlib.service.AuthorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {
    private static final Logger logger = LoggerFactory.getLogger(AuthorServiceImpl.class);


    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    CustomerRepository customerRepository;

    @Override
    @Transactional
    public Author addAuthor(String url) {
        Author author = authorRepository.findByLink(url);
        if(null != author){
            return author;
        }
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
    public Integer importAuthors(Collection<String> authorLinks) {
        for(String authorLink: authorLinks){
            addAuthor(authorLink);
        }
        return authorLinks.size();
    }

    @Override
    public List<String> exportAuthors() {
        return authorRepository.findAllAuthorLinks();
    }

    @Override
    public void delete(long authorId) {
        authorRepository.delete(authorId);
    }

}
