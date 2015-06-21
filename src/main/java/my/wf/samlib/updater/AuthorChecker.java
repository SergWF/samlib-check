package my.wf.samlib.updater;

import my.wf.samlib.exception.SamlibException;
import my.wf.samlib.model.dto.UpdatingProcessDto;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.model.repositoriy.AuthorRepository;
import my.wf.samlib.service.CustomerService;
import my.wf.samlib.tools.LinkTool;
import my.wf.samlib.updater.parser.SamlibAuthorParser;
import my.wf.samlib.updater.parser.SamlibPageReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


@Component
public class AuthorChecker {
    private static final Logger logger = LoggerFactory.getLogger(AuthorChecker.class);
    private static final AtomicBoolean updateFlag = new AtomicBoolean(false);

    private String linkSuffix;

    private AuthorRepository authorRepository;
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
    public void setSamlibAuthorParser(SamlibAuthorParser samlibAuthorParser) {
        this.samlibAuthorParser = samlibAuthorParser;
    }

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Autowired
    public void setSamlibPageReader(SamlibPageReader samlibPageReader) {
        this.samlibPageReader = samlibPageReader;
    }

    @Async
    public void doSomething(UUID uuid){
        System.out.println("begin " + uuid);
        for(int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
                System.out.println(i + "RUN " + uuid);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("end " + uuid);
    }

    @Async
    public UpdatingProcessDto checkAll() {
        if (updateFlag.get()) {
            return getUpdateStatus();
        }
        updateFlag.set(true);
        doCheckUpdates();
        updateFlag.set(false);
        return getUpdateStatus();
    }

    public UpdatingProcessDto getUpdateStatus() {
        return new UpdatingProcessDto(total.get(), processed.get());
    }

    public UpdatingProcessDto doCheckUpdates() {
        Set<Author> authors = authorRepository.findAllWithWritings();
        total.set(authors.size());
        processed.set(0);
        checkAuthors(authors, new Date());
        return getUpdateStatus();
    }

    @Async
    public void checkAuthors(Collection<Author> authors, Date checkDate){
        logger.info("checkAuthorUpdates authors, started at{} found {} records", checkDate, authors.size());
        for (Author author : authors) {
            try {
                System.out.println(">>>" + author);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                authorRepository.save(checkAuthorUpdates(author));
                processed.getAndAdd(1);
            } catch (SamlibException e) {
                logger.error("Can not checkAuthorUpdates author", e);
            }
        }
        logger.info("checkAuthorUpdates finished for {} authors", authors.size());
        customerService.updateUnreadWritings(checkDate);
        logger.info("Subscriptions were updated");
    }


    protected Author checkAuthorUpdates(Author author) {
        logger.info("check author {} by link {}", author.getName(), author.getLink());
        Date checkDate = new Date();
        author.setLink(LinkTool.getAuthorLink(author.getLink(), linkSuffix));
        String fullLink = LinkTool.getFullAuthorLink(author.getLink(), linkSuffix);
        String pageString = samlibPageReader.readPage(fullLink);
        logger.info("loaded {} symbols", pageString.length());
        String authorName = samlibAuthorParser.parseAuthorName(pageString);
        Set<Writing> parsedWritings = samlibAuthorParser.parseWritings(pageString);
        logger.info("name= {}, writings={}", authorName, parsedWritings);
        return authorRepository.save(implementChanges(author, parsedWritings, authorName, checkDate));
    }

    protected Author implementChanges(Author author, Collection<Writing> parsedWritings, String authorName, Date checkDate) {
        Map<Writing, Writing> writingMap = findSame(author.getWritings(), parsedWritings);
        Set<Writing> newWritings = new HashSet<>(parsedWritings);
        newWritings.removeAll(writingMap.values());
        author.setName(authorName);
        author = handleOldWritings(author, writingMap, checkDate);
        author.getWritings().addAll(newWritings);
        for (Writing writing : newWritings) {
            writing.setAuthor(author);
            writing.setLastChangedDate(checkDate);
        }
        return author;
    }

    protected Author handleOldWritings(Author author, Map<Writing, Writing> writingMap, Date checkDate) {
        int i = 0;
        for (Writing oldWriting : writingMap.keySet()) {
            Writing newWriting = writingMap.get(oldWriting);
            if (null == newWriting) {
                author.getWritings().remove(oldWriting);
            } else if (hasChanges(newWriting, oldWriting)) {
                oldWriting.setLastChangedDate(checkDate);
                oldWriting.setPrevSize(oldWriting.getSize());
                oldWriting.setSize(newWriting.getSize());
                oldWriting.setDescription(newWriting.getDescription());
                oldWriting.setGroupName(newWriting.getGroupName());
            }
        }
        return author;
    }

    protected boolean hasChanges(Writing newWriting, Writing oldWriting) {
        return !(isSame(newWriting.getDescription(), oldWriting.getDescription())
                && isSame(newWriting.getSize(), oldWriting.getSize())
                && isSame(newWriting.getGroupName(), oldWriting.getGroupName())
        );
    }

    protected boolean isSame(Object o1, Object o2){
        return null == o1 ? (null == o2):o1.equals(o2);
    }

    protected Map<Writing, Writing> findSame(Collection<Writing> oldWritings, Collection<Writing> newWritings) {
        Map<Writing, Writing> map = new HashMap<>(oldWritings.size());
        for (Writing oldWriting : oldWritings) {
            map.put(oldWriting, findSameWriting(newWritings, oldWriting));
        }
        return map;
    }

    protected Writing findSameWriting(Collection<Writing> writings, Writing baseWriting) {
        for (Writing writing : writings) {
            if (writing.getLink().equals(baseWriting.getLink())) {
                return writing;
            }
        }
        return null;
    }

}
