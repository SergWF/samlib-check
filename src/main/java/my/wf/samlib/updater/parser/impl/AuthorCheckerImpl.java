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
import java.util.Set;
import java.util.stream.Collectors;

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
        prepareWritings(parsedWritings, oldAuthor);
        Set<Writing> deleted = getDeleted(oldAuthor, parsedWritings);
        Set<Writing> newWritings = getNewWritings(oldAuthor, parsedWritings);
        Set<Writing> updatedWritings = getUpdatedWritings(oldAuthor, parsedWritings);
        return new AuthorDelta(oldAuthor, authorName, newWritings, updatedWritings, deleted);
    }

    void prepareWritings(Collection<Writing> parsedWritings, Author author) {
        parsedWritings.forEach(writing -> writing.setAuthor(author));
    }

    Set<Writing> getUpdatedWritings(Author oldAuthor, Collection<Writing> parsedWritings) {
        return parsedWritings.stream().filter(writing -> checkHasChanges(writing, oldAuthor)).collect(Collectors.toSet());
    }

    Set<Writing> getNewWritings(Author oldAuthor, Collection<Writing> parsedWritings) {
        return parsedWritings.stream().filter(writing -> checkIsNew(writing, oldAuthor)).collect(Collectors.toSet());
    }


    @Override
    public Author applyDelta(AuthorDelta delta) {
        Author author = delta.getAuthor();
        delta.getDeletedWritings().forEach(writing -> author.getWritings().remove(writing));
        delta.getUpdatedWritings().forEach(writing -> applyWritingChanges(writing, author.getWritings(), delta.getTimestamp()));
        author.getWritings().addAll(delta.getNewWritings());
        delta.getNewWritings().forEach(writing -> writing.setAuthor(author));
        author.setName(delta.getAuthorName());
        return author;
    }

    Set<Writing> getDeleted(Author oldAuthor, Collection<Writing> newWritings){
        return oldAuthor.getWritings().stream().filter(writing -> !isPresentByLink(newWritings, writing)).collect(Collectors.toSet());
    }

    boolean checkIsNew(Writing newWriting, Author oldAuthor){
        return !oldAuthor.getWritings().contains(newWriting);
    }

    boolean checkHasChanges(Writing newWriting, Author oldAuthor){
        Writing oldWriting = findSameWriting(oldAuthor.getWritings(), newWriting);
        return null != oldWriting
                &&(!newWriting.getSize().equals(oldWriting.getSize())
                || !newWriting.getDescription().equals(oldWriting.getDescription())
                || !newWriting.getName().equals(oldWriting.getName()));
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

    boolean isPresentByLink(Collection<Writing> writings, Writing baseWriting) {
        return writings.stream().filter((w) -> w.getLink().equals(baseWriting.getLink())).findFirst().isPresent();
    }
}
