package my.wf.samlib.updater.parser.impl;

import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.updater.parser.SamlibAuthorParser;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Scope(value = "prototype",  proxyMode = ScopedProxyMode.INTERFACES)
public class SamlibAuthorParserImpl implements SamlibAuthorParser {

    private static final Pattern namePattern = Pattern.compile("<center>\\s*<h3>(?<authorName>.*?):<br>");
    private static final Pattern writingPattern = Pattern.compile("<DL><DT><li>(?:<font.*?>.*?</font>)?\\s*(<b>(?<Authors>.*?)\\s*</b>\\s*)?<A HREF=(?<LinkToText>.*?)><b>\\s*(?<NameOfText>.*?)\\s*</b></A>.*?<b>(?<SizeOfText>\\d+)k</b>.*?<small>(?:Оценка:<b>(?<DescriptionOfRating>(?<rating>\\d+(?:\\.\\d+)?).*?)</b>.*?)?\\s*\"@*(?<Section>.*?)\"\\s*(?<Genres>.*?)?\\s*(?:<A HREF=\"(?<LinkToComments>.*?)\">Комментарии:\\s*(?<CommentsDescription>(?<CommentCount>\\d+).*?)</A>\\s*)?</small>.*?(?:<br><DD>(<font(.*?)?>)?(?<Description>.*?))?(</font><DD>.*?)?</DL>");

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

    protected String prepareString(String pageString){
        int start = pageString.indexOf("<dl>");
        int end = pageString.lastIndexOf("</dl>") + 5;
        String page = pageString.substring(start, end);
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
