package my.wf.samlib.updater.parser.impl;

import my.wf.samlib.model.dto.IpCheckState;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.tools.LinkTool;
import my.wf.samlib.updater.parser.AuthorChecker;
import my.wf.samlib.updater.parser.SamlibAuthorParser;
import my.wf.samlib.updater.parser.SamlibPageReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

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

    @Autowired
    public void setSamlibAuthorParser(SamlibAuthorParser samlibAuthorParser) {
        this.samlibAuthorParser = samlibAuthorParser;
    }

    @Autowired
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
        logger.debug("name: {}, writings found:{}", authorName, parsedWritings.size());
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
        logger.info("({}/{}) {}, [{}]", findUpdatedCount(author.getWritings(), checkDate), author.getWritings().size(), author.getName(), author.getLink());
        return author;
    }

    protected int findUpdatedCount(Collection<Writing> writings, Date checkDate){
        int count = 0;
        for(Writing writing: writings){
            if(0 >= checkDate.compareTo(writing.getLastChangedDate())){
                count++;
            }
        }
        return count;
    }

    protected Author handleOldWritings(Author author, Map<Writing, Writing> writingMap, Date checkDate) {
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
