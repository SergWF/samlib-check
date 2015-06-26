package my.wf.samlib.service.impl;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.model.repositoriy.AuthorRepository;
import my.wf.samlib.service.AuthorService;
import my.wf.samlib.service.CustomerService;
import my.wf.samlib.tools.LinkTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Set;

@Service
public class AuthorServiceImpl implements AuthorService {
    private static final Logger logger = LoggerFactory.getLogger(AuthorServiceImpl.class);


    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private CustomerService customerService;
    private String linkSuffix;

    @Value("${link.suffix}")
    public void setLinkSuffix(String linkSuffix) {
        this.linkSuffix = linkSuffix;
    }

    @Override
    @Transactional
    public Author addAuthor(String url) {
        Author author = authorRepository.findByLink(LinkTool.getAuthorLink(url, linkSuffix));
        if(null != author){
            return author;
        }
        author = createNewAuthor(url);
        return authorRepository.save(author);
    }

    protected Author createNewAuthor(String url) {
        Author author;
        author = new Author();
        author.setName("author " + url);
        author.setLink(url);
        return author;
    }

    @Override
    public Author findAuthor(long authorId) {
        return authorRepository.findOne(authorId);
    }

    @Override
    public Integer importAuthors(Customer customer, Collection<String> authorLinks) {
        for(String authorLink: authorLinks){
            customerService.addAuthor(customer, addAuthor(authorLink));
        }
        return authorLinks.size();
    }

    @Override
    public Set<String> exportAuthors() {
        return authorRepository.findAllAuthorLinks();
    }

    @Override
    public void delete(long authorId) {
        authorRepository.delete(authorId);
    }

}


