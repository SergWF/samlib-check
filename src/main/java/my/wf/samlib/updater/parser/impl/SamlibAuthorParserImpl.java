package my.wf.samlib.updater.parser.impl;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.updater.parser.SamlibAuthorParser;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Scope(value = "prototype",  proxyMode = ScopedProxyMode.INTERFACES)
public class SamlibAuthorParserImpl implements SamlibAuthorParser {

    private static final Pattern namePattern = Pattern.compile("<center>\\s*<h3>(?<authorName>.*?):<br>");
    private static final Pattern writingPattern = Pattern.compile("<DL><DT><li>(?:<font.*?>.*?</font>)?\\s*(<b>(?<Authors>.*?)\\s*</b>\\s*)?<A HREF=(?<LinkToText>.*?)><b>\\s*(?<NameOfText>.*?)\\s*</b></A>.*?<b>(?<SizeOfText>\\d+)k</b>.*?<small>(?:Оценка:<b>(?<DescriptionOfRating>(?<rating>\\d+(?:\\.\\d+)?).*?)</b>.*?)?\\s*\"@*(?<Section>.*?)\"\\s*(?<Genres>.*?)?\\s*(?:<A HREF=\"(?<LinkToComments>.*?)\">Комментарии:\\s*(?<CommentsDescription>(?<CommentCount>\\d+).*?)</A>\\s*)?</small>.*?(?:<br><DD>(<font(.*?)?>)?(?<Description>.*?))?(</font><DD>.*?)?</DL>");

    @Override
    public Author parse(String authorLink, String page){
        Author author = new Author();
        author.setLink(authorLink);
        String pageString = prepareString(page);
        author.setName(extractAuthorName(page));
        author.getWritings().addAll(extractWritings(pageString, author));
        return author;
    }

    protected String extractAuthorName(String page) {
        Matcher matcher = namePattern.matcher(page);
        return matcher.find()?matcher.group("authorName"):null;
    }

    protected List<Writing> extractWritings(String pageString, Author author){
        Matcher matcher = writingPattern.matcher(pageString);
        List<Writing> writings = new ArrayList<>();
        while(matcher.find()){
            Writing writing = fromMatcher(matcher);
            writing.setAuthor(author);
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
        w.setDescription(matcher.group("Description"));
        w.setSize(matcher.group("SizeOfText"));
        w.setGroupName(matcher.group("Section"));
        return w;
    }

}
