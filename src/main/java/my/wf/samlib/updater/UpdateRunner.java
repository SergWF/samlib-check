package my.wf.samlib.updater;

import my.wf.samlib.exception.SamlibException;
import my.wf.samlib.model.dto.UpdatingProcessDto;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.service.AuthorService;
import my.wf.samlib.service.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class UpdateRunner {

    private static final Logger logger = LoggerFactory.getLogger(UpdateRunner.class);

    @Autowired
    AuthorCheckerFactory authorCheckerFactory;
    @Autowired
    private AuthorService authorService;
    @Autowired
    SubscriptionService subscriptionService;
    private UpdatingProcessDto updatingProcessDto = null;
    private AtomicBoolean inProcessFlag = new AtomicBoolean(false);
    private Long pauseBetweenAuthors;

    @Value("${pause.between.authors:1000}")
    public void setPauseBetweenAuthors(Long pauseBetweenAuthors) {
        this.pauseBetweenAuthors = pauseBetweenAuthors;
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
        return authorCheckerFactory.getAuthorChecker().checkIpState();
    }

    protected void doUpdate(Date checkDate){
        if(isCanUpdate()){
            return;
        }
        List<Author> authors = authorService.findAllAuthors();
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
            }catch (SamlibException e){
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


    protected Author doUpdateAuthor(Author author, Date checkDate){
        author = authorService.findAuthorWithWritings(author.getId());
        Author updated = authorCheckerFactory.getAuthorChecker().checkAuthorUpdates(author);
        subscriptionService.updateUnreadState(authorService.saveAuthor(updated), checkDate);
        return updated;
    }

}
