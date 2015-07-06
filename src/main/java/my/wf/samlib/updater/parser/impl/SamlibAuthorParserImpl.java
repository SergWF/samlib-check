package my.wf.samlib.updater.parser.impl;

import my.wf.samlib.model.dto.IpCheckState;
import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.updater.parser.SamlibAuthorParser;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SamlibAuthorParserImpl implements SamlibAuthorParser {

    private static final Pattern namePattern = Pattern.compile("<center>\\s*<h3>(?<authorName>.*?):<br>");
    private static final Pattern writingPattern = Pattern.compile("<DL><DT><li>(?:<font.*?>.*?</font>)?\\s*(<b>(?<Authors>.*?)\\s*</b>\\s*)?<A HREF=(?<LinkToText>.*?)><b>\\s*(?<NameOfText>.*?)\\s*</b></A>.*?<b>(?<SizeOfText>\\d+)k</b>.*?<small>(?:Оценка:<b>(?<DescriptionOfRating>(?<rating>\\d+(?:\\.\\d+)?).*?)</b>.*?)?\\s*\"@*(?<Section>.*?)\"\\s*(?<Genres>.*?)?\\s*(?:<A HREF=\"(?<LinkToComments>.*?)\">Комментарии:\\s*(?<CommentsDescription>(?<CommentCount>\\d+).*?)</A>\\s*)?</small>.*?(?:<br><DD>(<font(.*?)?>)?(?<Description>.*?))?(</font><DD>.*?)?</DL>");
    private static final Pattern checkStatePattern = Pattern.compile("<pre>\\s*(?<info>.*?IP:\\s(?<ip>\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}).*?1\\.\\s*(?<inSpam>.*?)2\\.(?<Blocked>.*?))</pre>");

    private static final String NOT_IN_SPAM="(not in spam-list)";
    private static final String NOT_IS_BLOCKED="не блокирован";

    @Override
    public String parseAuthorName(String pageString) {
        Matcher matcher = namePattern.matcher(pageString);
        return matcher.find()?matcher.group("authorName"):null;
    }

    @Override
    public Set<Writing> parseWritings(String pageString) {
        Matcher matcher = writingPattern.matcher(pageString);
        Set<Writing> writings = new HashSet<>();
        while(matcher.find()){
            Writing writing = fromMatcher(matcher);
            writings.add(writing);
        }
        return writings;
    }

    @Override
    public IpCheckState parseIpCheckState(String checkPageString) {
        Matcher matcher = checkStatePattern.matcher(removeSpaces(checkPageString));
        IpCheckState ipCheckState = new IpCheckState();
        if(matcher.find()){
            System.out.println("info: " + matcher.group("info"));
            System.out.println("ip: " + matcher.group("ip"));
            System.out.println("inSpam: " + matcher.group("inSpam"));
            System.out.println("Blocked: " + matcher.group("Blocked"));

            ipCheckState.setInfo(matcher.group("info"));
            ipCheckState.setInSpamList(getInSpamValue(matcher.group("inSpam")));
            ipCheckState.setBlocked(getIsBlockedValue(matcher.group("Blocked")));
            ipCheckState.setIp(matcher.group("ip"));
        }else{
            System.out.println("---");
        }
        return ipCheckState;
    }

    private boolean getIsBlockedValue(String blocked) {
        return null != blocked && !blocked.contains(NOT_IS_BLOCKED);
    }

    private boolean getInSpamValue(String inSpam) {
        return null  != inSpam && !inSpam.contains(NOT_IN_SPAM);
    }

    protected String prepareString(String pageString){
        int start = pageString.indexOf("<dl>");
        int end = pageString.lastIndexOf("</dl>") + 5;
        String page = pageString.substring(start, end);
        return removeSpaces(page);
    }

    protected String removeSpaces(String page){
        String s = Pattern.compile("\\s+").matcher(page).replaceAll(" ");
        return Pattern.compile(">\\s+<").matcher(s).replaceAll("><");
    }

    private Writing fromMatcher(Matcher matcher){
        Writing w = new Writing();
        w.setName(matcher.group("NameOfText"));
        w.setLink(matcher.group("LinkToText"));
        String description = matcher.group("Description");
        if(null != description) {
            w.setDescription(description.replaceAll("\\<[^>]*>", ""));
        }
        w.setSize(matcher.group("SizeOfText"));
        w.setGroupName(matcher.group("Section"));
        return w;
    }

}
