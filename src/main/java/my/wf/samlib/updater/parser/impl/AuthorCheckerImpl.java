package my.wf.samlib.updater.parser.impl;

import my.wf.samlib.model.dto.IpCheckState;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Changed;
import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.tools.LinkTool;
import my.wf.samlib.updater.parser.AuthorChecker;
import my.wf.samlib.updater.parser.SamlibAuthorParser;
import my.wf.samlib.updater.parser.SamlibPageReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class AuthorCheckerImpl implements AuthorChecker {

    private static final Logger logger = LoggerFactory.getLogger(AuthorCheckerImpl.class);

    private String banCheckUrl;
    private String linkSuffix;

    private SamlibAuthorParser samlibAuthorParser;
    private SamlibPageReader samlibPageReader;

    public void setLinkSuffix(String linkSuffix) {
        this.linkSuffix = linkSuffix;
    }

    public void setBanCheckUrl(String banCheckUrl) {
        this.banCheckUrl = banCheckUrl;
    }

    public void setSamlibAuthorParser(SamlibAuthorParser samlibAuthorParser) {
        this.samlibAuthorParser = samlibAuthorParser;
    }

    public void setSamlibPageReader(SamlibPageReader samlibPageReader) {
        this.samlibPageReader = samlibPageReader;
    }

    @Override
    public Author checkAuthorUpdates(Author author) {
        logger.debug("check author {} by link {}", author.getName(), author.getLink());
        Date checkDate = new Date();
        author.setLink(LinkTool.getAuthorLink(author.getLink(), linkSuffix));
        String fullLink = LinkTool.getAuthorIndexPage(author.getLink(), linkSuffix);
        String pageString = samlibPageReader.readPage(fullLink);
        logger.debug("loaded {} symbols", pageString.length());
        String authorName = samlibAuthorParser.parseAuthorName(pageString);
        Set<Writing> parsedWritings = samlibAuthorParser.parseWritings(pageString);
        logger.debug("author: {}, writings found:{}", authorName, parsedWritings.size());
        return implementChanges(author, parsedWritings, authorName, checkDate);
    }

    @Override
    public boolean checkIpState() {
        IpCheckState ipCheckState = new IpCheckState();
        try{
            logger.debug("check IP state {}", banCheckUrl);
            String checkPage = samlibPageReader.readPage(banCheckUrl);
            ipCheckState = samlibAuthorParser.parseIpCheckState(checkPage);
        }catch (Exception e){
            ipCheckState.setInfo(e.getMessage());
            ipCheckState.setOtherError(true);
        }
        printCheckState(ipCheckState);
        return ipCheckState.isOk();
    }
    private void printCheckState(IpCheckState ipCheckState){
        logger.info("ip: {}, in spam: {}, is blocked: {}, other: {}", ipCheckState.getIp(), ipCheckState.isInSpamList(), ipCheckState.isBlocked(), ipCheckState.isOtherError());
        if(!ipCheckState.isOk()){
            logger.error("IP Check problems:");
            logger.error(ipCheckState.getInfo());
        }
    }

    protected Author implementChanges(Author author, Collection<Writing> parsedWritings, String authorName, Date checkDate) {
        author.setName(authorName);
        Collection<Writing> oldWritings = new HashSet<>(author.getWritings());
        author.getWritings().clear();
        author.getWritings().addAll(parsedWritings);
        author.getWritings().stream().forEach(
                (w)->{
                    w.setAuthor(author);
                    applyChanges(w, oldWritings, checkDate);
                });
        return author;
    }

    protected Writing applyChanges(Writing writing, Collection<Writing> oldWritings, Date checkDate) {
        Writing old = findSameWriting(oldWritings, writing);
        if(null ==old){
            writing.getChangesIn().add(Changed.NEW);
        } else {
            writing.setUnread(old.isUnread());
            writing.setLastChangedDate(old.getLastChangedDate());
            if(!writing.getName()
                    .equals(old.getName())) {
                writing.getChangesIn()
                        .add(Changed.NAME);
            }
            if(!writing.getDescription()
                    .equals(old.getDescription())) {
                writing.getChangesIn()
                        .add(Changed.DESCRIPTION);
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



    protected Writing findSameWriting(Collection<Writing> writings, Writing baseWriting) {
        return writings.stream().filter((w) -> w.getLink().equals(baseWriting.getLink())).findFirst().orElse(null);
    }
}
