package my.wf.samlib.updater;

import my.wf.samlib.exception.PageReadException;
import my.wf.samlib.exception.SamlibException;
import my.wf.samlib.model.dto.UpdatingProcessDto;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.model.repositoriy.AuthorRepository;
import my.wf.samlib.updater.parser.SamlibAuthorParser;
import my.wf.samlib.updater.parser.SamlibPageReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


@Component
public class AuthorChecker {
    private static final Logger logger = LoggerFactory.getLogger(AuthorChecker.class);
    private static final AtomicBoolean updateFlag = new AtomicBoolean(false);

    @Autowired
    AuthorRepository authorRepository;

    private static final AtomicInteger total = new AtomicInteger(0);
    private static final AtomicInteger processed = new AtomicInteger(0);

    @Async
    public UpdatingProcessDto checkAll(){
        if(updateFlag.get()){
            return getProcess();
        }
        updateFlag.set(true);
        doUpdate();
        updateFlag.set(false);
        return getProcess();
    }

    protected UpdatingProcessDto getProcess(){
        return new UpdatingProcessDto(total.get(), processed.get());
    }

    protected void doUpdate(){
        List<Author> authors = authorRepository.findAll();
        total.set(authors.size());
        processed.set(0);
        logger.info("update authors, found {} records", authors.size());
        for(Author author: authors){
            try {
                authorRepository.save(update(author));
                processed.getAndAdd(1);
            } catch (SamlibException e) {
                logger.error("Can not update author", e);
            }
        }
        logger.info("update finished for {} authors", authors.size());
    }


    Author update(Author author) {
        logger.info("check author {}", author.getName());
        Date checkDate = new Date();
        int updateCount = 0;
        String pageString = null;
        try {
            pageString = new SamlibPageReader().readPage(author.getLink());
        } catch (IOException e) {
            throw new PageReadException(e);
        }
        Author newAuthor = new SamlibAuthorParser().parse(author.getLink(), pageString);
        for(Writing writing: newAuthor.getWritings()){
            Writing oldWriting = findOldWriting(author, writing.getLink());
            if(checkUpdated(writing, oldWriting)){
                writing.setLastChangedDate(checkDate);
                writing.setPrevSize((null == oldWriting)?null:oldWriting.getSize());
            }
        }
        logger.info("checked {},  {} new writings", author.getName(), updateCount);
        return newAuthor;
    }

    protected boolean checkUpdated(Writing newWriting, Writing oldWriting){
        return !(null != oldWriting
                && newWriting.getDescription().equals(oldWriting.getDescription())
                && newWriting.getSize().equals(oldWriting.getSize())
                && newWriting.getGroupName().equals(oldWriting.getGroupName())
                && newWriting.getSamlibDate().after(oldWriting.getSamlibDate())
        );

    }

    protected Writing findOldWriting(Author author, String writingLink){
        for(Writing writing: author.getWritings()){
            if(writing.getLink().equals(writingLink)){
                return writing;
            }
        }
        return null;
    }
}
