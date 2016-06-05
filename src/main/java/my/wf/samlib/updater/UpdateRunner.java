package my.wf.samlib.updater;

import my.wf.samlib.exception.SamlibException;
import my.wf.samlib.model.dto.UpdatingProcessDto;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.service.AuthorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
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


    @Value("${pause.between.authors:20000}")
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
        doUpdate(new Date());
        inProcessFlag.set(false);
    }

    private int findUpdatedWritingCount(Author author, Date checkDateStart) {
        int count = 0;
        for(Writing writing: author.getWritings()){
            if(0 >= checkDateStart.compareTo(writing.getLastChangedDate())){
                count++;
            }
        }
        return count;
    }

    protected boolean isCanUpdate(){
        return skipBanUrlChecking || authorCheckerFactory.getAuthorChecker().checkIpState();
    }

    protected void doUpdate(Date checkDate){
        if(!isCanUpdate()){
            logger.error("Problems with IP Checking");
            return;
        }
        Set<Author> authors = authorService.findAllAuthors();
        updatingProcessDto = new UpdatingProcessDto();
        updatingProcessDto.setProcessed(0);
        updatingProcessDto.setTotal(authors.size());
        updatingProcessDto.setDate(checkDate);
        for(Author author: authors){
            try {
                Author updated = doUpdateAuthor(author, checkDate);
                updatingProcessDto.getAuthorsUpdated().putIfAbsent(updated, findUpdatedWritingCount(updated, checkDate));
                logger.debug("Updated {} writings for author {}", updatingProcessDto.getAuthorsUpdated().get(updated), updated.getName());
                makePause();
            }catch (IOException | SamlibException e){
                logger.error("Exception on Author update", e);
            }
        }
        printUpdateResult();
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
        for(Author author: updatingProcessDto.getAuthorsUpdated().keySet()){
            if(0 < updatingProcessDto.getAuthorsUpdated().get(author)) {
                logger.info("({})\t{}", updatingProcessDto.getAuthorsUpdated().get(author), author.getName());
            }
        }
    }


    protected Author doUpdateAuthor(Author author, Date checkDate) throws IOException {
        author = authorService.findAuthor(author.getId());
        Author updated = authorCheckerFactory.getAuthorChecker().checkAuthorUpdates(author);
        return authorService.saveAuthor(updated);
    }

}
