package my.wf.samlib.updater.parser.impl;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Changed;
import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.tools.LinkTool;
import my.wf.samlib.updater.AuthorDelta;
import my.wf.samlib.updater.parser.AuthorChecker;
import my.wf.samlib.updater.parser.SamlibAuthorParser;
import my.wf.samlib.updater.parser.SamlibPageReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AuthorCheckerImpl implements AuthorChecker {

    private static final Logger logger = LoggerFactory.getLogger(AuthorCheckerImpl.class);

    private String linkSuffix;

    private SamlibAuthorParser samlibAuthorParser;
    private SamlibPageReader samlibPageReader;

    public void setLinkSuffix(String linkSuffix) {
        this.linkSuffix = linkSuffix;
    }


    public void setSamlibAuthorParser(SamlibAuthorParser samlibAuthorParser) {
        this.samlibAuthorParser = samlibAuthorParser;
    }

    public void setSamlibPageReader(SamlibPageReader samlibPageReader) {
        this.samlibPageReader = samlibPageReader;
    }

    @Override
    public AuthorDelta checkAuthorUpdates(Author author, LocalDateTime checkDate) {
        logger.debug("check author {} by link {}", author.getName(), author.getLink());
        author.setLink(LinkTool.getAuthorLink(author.getLink(), linkSuffix));
        String fullLink = LinkTool.getAuthorIndexPage(author.getLink(), linkSuffix);
        String pageString = samlibPageReader.readPage(fullLink);
        logger.debug("loaded {} symbols", pageString.length());
        String authorName = samlibAuthorParser.parseAuthorName(pageString);
        Set<Writing> parsedWritings = samlibAuthorParser.parseWritings(pageString);
        logger.debug("author: {}, writings found:{}", authorName, parsedWritings.size());
        return createDelta(author, authorName, parsedWritings);
    }

    AuthorDelta createDelta(Author oldAuthor, String authorName, Collection<Writing> parsedWritings){
        AuthorDelta delta = new AuthorDelta(oldAuthor);
        delta.setAuthorName(authorName);
        parsedWritings.forEach(writing -> processParsed(oldAuthor, writing, delta));
        delta.getDeletedWritings().addAll(oldAuthor.getWritings());
        delta.getDeletedWritings().removeAll(parsedWritings);
        return delta;
    }


    @Override
    public Author applyChanges(AuthorDelta delta) {
        Author author = delta.getAuthor();
        delta.getDeletedWritings().forEach(writing -> author.getWritings().remove(writing));
        delta.getUpdatedWritings().forEach(writing -> applyWritingChanges(writing, author.getWritings(), delta.getTimestamp()));
        author.getWritings().addAll(delta.getNewWritings());
        delta.getNewWritings().forEach(writing -> writing.setAuthor(author));
        author.setName(delta.getAuthorName());
        return author;
    }


    void processParsed(Author oldAuthor, Writing writing, AuthorDelta delta) {
        writing.setAuthor(oldAuthor);
        Writing existsing = findSameWriting(oldAuthor.getWritings(), writing);
        if(null == existsing){
            delta.getNewWritings().add(writing);
        }else{
            if(checkHasChanges(existsing, writing)){
                delta.getUpdatedWritings().add(writing);
            }
        }
    }

    boolean checkHasChanges(Writing writing1, Writing writing2){
        return !writing1.getSize().equals(writing2.getSize())
                || !writing1.getDescription().equals(writing2.getDescription())
                || !writing1.getName().equals(writing2.getName());
    }



    Writing applyWritingChanges(Writing writing, Collection<Writing> oldWritings, LocalDateTime checkDate) {
        Writing old = findSameWriting(oldWritings, writing);
        if(null ==old){
            writing.getChangesIn().add(Changed.NEW);
        } else {
            writing.setUnread(old.isUnread());
            writing.setLastChangedDate(old.getLastChangedDate());
            if(!writing.getName().equals(old.getName())) {
                writing.getChangesIn().add(Changed.NAME);
            }
            if(!writing.getDescription().equals(old.getDescription())) {
                writing.getChangesIn().add(Changed.DESCRIPTION);
            }
            if(!writing.getSize().equals(old.getSize())) {
                writing.getChangesIn().add(Changed.SIZE);
                writing.setPrevSize(old.getSize());
            }else{
                writing.setPrevSize(old.getPrevSize());
            }
        }
        if(!writing.getChangesIn().isEmpty()){
            writing.setLastChangedDate(checkDate);
            writing.setUnread(true);
        }
        return writing;
    }



    Writing findSameWriting(Collection<Writing> writings, Writing baseWriting) {
        return writings.stream().filter((w) -> w.getLink().equals(baseWriting.getLink())).findFirst().orElse(null);
    }
}
