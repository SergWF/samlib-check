package my.wf.samlib.updater;

import my.wf.samlib.exception.SamlibException;
import my.wf.samlib.model.dto.UpdatingProcessDto;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.service.AuthorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class UpdateRunner {

    private static final Logger logger = LoggerFactory.getLogger(UpdateRunner.class);

    @Autowired
    AuthorCheckerFactory authorCheckerFactory;
    @Autowired
    private AuthorService authorService;

    private UpdatingProcessDto updatingProcessDto = null;
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

    @Async
    public void runUpdate(){
        if (inProcessFlag.get()) {
            logger.info("Update is already started");
                return;
        }
        inProcessFlag.set(true);
        doUpdate(LocalDateTime.now());
        inProcessFlag.set(false);
    }

    private int findUpdatedWritingCount(Author author, LocalDateTime checkDateStart) {
        int count = 0;
        for(Writing writing: author.getWritings()){
            if(0 >= checkDateStart.compareTo(writing.getLastChangedDate())){
                count++;
            }
        }
        return count;
    }

    private boolean isCanUpdate(){
        return skipBanUrlChecking || authorCheckerFactory.getAuthorChecker().checkIpState();
    }

    public void doUpdate(LocalDateTime checkDate){
        if(!isCanUpdate()){
            logger.error("Problems with IP Checking");
            return;
        }
        Set<Author> authors = authorService.findAllAuthors();
        updatingProcessDto = new UpdatingProcessDto();
        updatingProcessDto.setProcessed(0);
        updatingProcessDto.setTotal(authors.size());
        updatingProcessDto.setDate(checkDate);
        authors.stream().forEach((a)->processAuthorUpdate(a, checkDate));
        printUpdateResult();
    }

    private void processAuthorUpdate(Author author, LocalDateTime checkDate){
        try {
            Author updated = doUpdateAuthor(author, checkDate);
            updatingProcessDto.getAuthorsUpdated().putIfAbsent(updated, findUpdatedWritingCount(updated, checkDate));
            logger.debug("Updated {} writings for author {}", updatingProcessDto.getAuthorsUpdated().get(updated), updated.getName());
            authorService.setLastUpdateDate(checkDate);
            makePause();
        }catch (IOException | SamlibException e){
            logger.error("Exception on Author update", e);
        }
    }

    private void makePause(){
        logger.debug("make pause {} ms between checking authors", pauseBetweenAuthors);
        try {
            Thread.sleep(pauseBetweenAuthors);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void printUpdateResult() {
        logger.info("checked {} authors ", updatingProcessDto.getAuthorsUpdated().size());
        updatingProcessDto.getAuthorsUpdated().entrySet()
                .stream()
                .filter((entry) -> 0 < entry.getValue())
                .forEach((entry)->
                        logger.info("({})\t{}",entry.getValue(), entry.getKey().getName())
                );
    }


    private Author doUpdateAuthor(Author author, LocalDateTime checkDate) throws IOException {
        author = authorService.findAuthor(author.getId());
        Author updated = authorCheckerFactory.getAuthorChecker().checkAuthorUpdates(author, checkDate);
        return authorService.saveAuthor(updated);
    }

}
