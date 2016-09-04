package my.wf.samlib.updater.impl;

import my.wf.samlib.exception.SamlibException;
import my.wf.samlib.model.dto.UpdatingInfo;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.service.AuthorService;
import my.wf.samlib.updater.AuthorCheckerFactory;
import my.wf.samlib.updater.AuthorDelta;
import my.wf.samlib.updater.UpdateRunner;
import my.wf.samlib.updater.parser.AuthorChecker;
import my.wf.samlib.updater.parser.BanCheckerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Component

public class UpdateRunnerImpl implements UpdateRunner {
    private static final Logger logger = LoggerFactory.getLogger(UpdateRunner.class);

    @Autowired
    private AuthorCheckerFactory authorCheckerFactory;
    @Autowired
    private BanCheckerFactory banCheckerFactory;
    @Autowired
    private AuthorService authorService;
    @Value("${ban.check.url}")
    private String banCheckUrl;

    private volatile UpdatingInfo updatingInfo = null;
    private AtomicBoolean inProcessFlag = new AtomicBoolean(false);
    private Long pauseBetweenAuthors;
    private boolean skipBanUrlChecking;


    @Value("${pause.between.authors:10000}")
    public void setPauseBetweenAuthors(Long pauseBetweenAuthors) {
        this.pauseBetweenAuthors = pauseBetweenAuthors;
    }

    @Value("${skip.ban.url.checking:false}")
    public void setSkipBanUrlChecking(boolean skipBanUrlChecking) {
        this.skipBanUrlChecking = skipBanUrlChecking;
    }

    boolean canUpdate(){
        return skipBanUrlChecking || banCheckerFactory.getBanChecker().checkIpState(banCheckUrl);
    }

    @Override
    public void doUpdate(LocalDateTime checkDate){
        if(inProcessFlag.get()){
            logger.warn("Trying to start new update before previous is finished");
            return;
        }
        inProcessFlag.set(true);
        try {
            if(!canUpdate()){
                logger.error("Problems with IP Checking");
                return;
            }
            doAllAuthorsUpdate(checkDate);
        } finally {
            inProcessFlag.set(false);
        }
    }

    void doAllAuthorsUpdate(LocalDateTime checkDate){
        Set<Author> authors = authorService.findAllAuthors();
        updatingInfo = new UpdatingInfo();
        updatingInfo.setTotal(authors.size());
        updatingInfo.setDate(checkDate);
        authors.stream()
                .forEach((a) -> {
                    processAuthorUpdate(a, checkDate, updatingInfo);
                    makePause();
                });
        printUpdateResult(updatingInfo);
    }

    void processAuthorUpdate(Author author, LocalDateTime checkDate, UpdatingInfo updatingInfo){
        try {
            AuthorDelta delta = doUpdateAuthor(author, checkDate);
            updatingInfo.getAuthorsUpdates().add(delta);
            logger.debug("Author {}: Added {}, Updated {} writings", delta.getAuthor().getName(), delta.getNewWritings().size(), delta.getUpdatedWritings().size());
            authorService.setLastUpdateDate(checkDate);
        }catch (IOException | SamlibException e){
            logger.error("Exception on Author update", e);
        }
    }

    void makePause(){
        logger.debug("make pause {} ms between checking authors", pauseBetweenAuthors);
        try {
            Thread.sleep(pauseBetweenAuthors);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void printUpdateResult(UpdatingInfo updatingInfo) {
        logger.info("checked {} authors from {} ", updatingInfo.getAuthorsUpdates().size(), updatingInfo.getTotal());
        updatingInfo.getAuthorsUpdates()
                .stream()
                .filter((delta) -> delta.hasChanges())
                .forEach((delta)->
                        logger.info("({}+{})\t{}",delta.getNewWritings().size(), delta.getUpdatedWritings().size(), delta.getAuthor().getName())
                );
    }


    AuthorDelta doUpdateAuthor(Author author, LocalDateTime checkDate) throws IOException {
        author = authorService.findAuthor(author.getId());
        AuthorChecker authorChecker = authorCheckerFactory.getAuthorChecker();
        AuthorDelta delta = authorChecker.checkAuthorUpdates(author, checkDate);
        author = authorChecker.applyDelta(delta);
        authorService.saveAuthor(author);
        return delta;
    }

    @Override
    public UpdatingInfo getState() {
        return updatingInfo;
    }
}
