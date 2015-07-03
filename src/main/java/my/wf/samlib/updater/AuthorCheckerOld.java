package my.wf.samlib.updater;

import my.wf.samlib.model.dto.UpdatingProcessDto;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.model.repositoriy.AuthorRepository;
import my.wf.samlib.tools.LinkTool;
import my.wf.samlib.updater.parser.AuthorCheckCallback;
import my.wf.samlib.updater.parser.SamlibAuthorParser;
import my.wf.samlib.updater.parser.SamlibPageReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


public class AuthorCheckerOld {
    private static final Logger logger = LoggerFactory.getLogger(AuthorCheckerOld.class);
    private static final AtomicBoolean updateFlag = new AtomicBoolean(false);

    private String linkSuffix;

    private AuthorRepository authorRepository;
    private SamlibAuthorParser samlibAuthorParser;
    private SamlibPageReader samlibPageReader;

    private int checkPauseMs;

    @Value("${check.pause.ms:1000}")
    public void setCheckPauseMs(int checkPauseMs) {
        this.checkPauseMs = checkPauseMs;
    }

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
    public void setSamlibPageReader(SamlibPageReader samlibPageReader) {
        this.samlibPageReader = samlibPageReader;
    }

    public UpdatingProcessDto checkAuthors(Collection<Author> authors, Date checkDate, AuthorCheckCallback callback){
        logger.info("checkAuthorUpdates authors, started at{} found {} records", checkDate, authors.size());
        UpdatingProcessDto updatingProcessDto = new UpdatingProcessDto();
        updatingProcessDto.setDate(checkDate);
        updatingProcessDto.setProcessed(0);
        updatingProcessDto.setTotal(authors.size());
        int updatedCount = 0;
        for (Author author : authors) {
            try {
                callback.onAuthorCheck(checkAuthorUpdates(author));
                updatingProcessDto.increaseProcessed(1);
            } catch (RuntimeException e) {
                updatingProcessDto.getErrors().putIfAbsent(author, e);
                logger.error("Can not checkAuthorUpdates author", e);
            }
        }
        logger.info("checkAuthorUpdates finished for {} authors. updated {} writings", authors.size(), updatedCount);
        return updatingProcessDto;
    }

    protected Author checkAuthorUpdates(Author author) {
        logger.debug("check author {} by link {}", author.getName(), author.getLink());
        Date checkDate = new Date();
        author.setLink(LinkTool.getAuthorLink(author.getLink(), linkSuffix));
        String fullLink = LinkTool.getAuthorIndexPage(author.getLink(), linkSuffix);
        String pageString = samlibPageReader.readPage(fullLink);
        logger.debug("loaded {} symbols", pageString.length());
        String authorName = samlibAuthorParser.parseAuthorName(pageString);
        Set<Writing> parsedWritings = samlibAuthorParser.parseWritings(pageString);
        logger.debug("name= {}, writings={}", authorName, parsedWritings);
        Author updatedAuthor = implementChanges(author, parsedWritings, authorName, checkDate);
        return authorRepository.save(updatedAuthor);
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
