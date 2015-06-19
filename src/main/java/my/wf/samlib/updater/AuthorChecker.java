package my.wf.samlib.updater;

import my.wf.samlib.exception.SamlibException;
import my.wf.samlib.model.dto.UpdatingProcessDto;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.repositoriy.AuthorRepository;
import my.wf.samlib.service.CustomerService;
import my.wf.samlib.updater.parser.AuthorChangesChecker;
import my.wf.samlib.updater.parser.SamlibAuthorParser;
import my.wf.samlib.updater.parser.SamlibPageReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


@Component
public class AuthorChecker {
    private static final Logger logger = LoggerFactory.getLogger(AuthorChecker.class);
    private static final AtomicBoolean updateFlag = new AtomicBoolean(false);

    private String linkSuffix;

    private AuthorRepository authorRepository;
    private AuthorChangesChecker authorChangesChecker;
    private SamlibAuthorParser samlibAuthorParser;
    private SamlibPageReader samlibPageReader;
    private CustomerService customerService;

    private static final AtomicInteger total = new AtomicInteger(0);
    private static final AtomicInteger processed = new AtomicInteger(0);


    @Value("${link.suffix}")
    public void setLinkSuffix(String linkSuffix) {
        this.linkSuffix = linkSuffix;
    }

    @Autowired
    public void setAuthorRepository(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Autowired
    public void setAuthorChangesChecker(AuthorChangesChecker authorChangesChecker) {
        this.authorChangesChecker = authorChangesChecker;
    }

    @Autowired
    public void setSamlibAuthorParser(SamlibAuthorParser samlibAuthorParser) {
        this.samlibAuthorParser = samlibAuthorParser;
    }

    @Autowired
    public CustomerService getCustomerService() {
        return customerService;
    }

    @Autowired
    public void setSamlibPageReader(SamlibPageReader samlibPageReader) {
        this.samlibPageReader = samlibPageReader;
    }

    @Async
    public UpdatingProcessDto checkAll(){
        if(updateFlag.get()){
            return getProcess();
        }
        updateFlag.set(true);
        doCheckUpdates();
        updateFlag.set(false);
        return getProcess();
    }

    protected UpdatingProcessDto getProcess(){
        return new UpdatingProcessDto(total.get(), processed.get());
    }

    protected void doCheckUpdates(){
        List<Author> authors = authorRepository.findAllWithWritings();
        total.set(authors.size());
        processed.set(0);
        logger.info("checkUpdates authors, found {} records", authors.size());
        Date checkDate = new Date();
        for(Author author: authors){
            try {
                authorRepository.save(checkUpdates(author));
                processed.getAndAdd(1);
            } catch (SamlibException e) {
                logger.error("Can not checkUpdates author", e);
            }
        }
        customerService.updateUnreadWritings(checkDate);
        logger.info("checkUpdates finished for {} authors", authors.size());
    }




    protected Author checkUpdates(Author author) {
        logger.info("check author {}", author.getName());
        Date checkDate = new Date();
        int updateCount = 0;
        String fullLink = getFullAuthorLink(author.getLink());
        String pageString = samlibPageReader.readPage(fullLink);
        Author newAuthor = samlibAuthorParser.parse(author.getLink(), pageString);
        newAuthor.setId(author.getId());
        authorChangesChecker.checkUpdatedWritings(newAuthor, author, checkDate);
        logger.info("checked {},  {} new writings", author.getName(), updateCount);
        return newAuthor;
    }

    protected String getShortAuthorLink(String authorBaseLink){
        return authorBaseLink.endsWith(linkSuffix)
                ? authorBaseLink.substring(0, authorBaseLink.length() - linkSuffix.length())
                : authorBaseLink;
    }

    protected String getFullAuthorLink(String authorBaseLink){
        return authorBaseLink.endsWith(linkSuffix)
                ? authorBaseLink
                :(authorBaseLink.endsWith("/")?authorBaseLink + linkSuffix: authorBaseLink +"/"+ linkSuffix);

    }


}
