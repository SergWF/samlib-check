package my.wf.samlib.updater.parser.impl;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Writing;
import org.apache.commons.io.IOUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Set;


public class SamlibAuthorParserImplTest {


    private SamlibAuthorParserImpl samlibAuthorParser;

    private String fullWritingString="<DL><DT><li><A HREF=vs2.shtml><b>Воздушный стрелок 2</b></A> &nbsp; <b>612k</b> &nbsp; <small>Оценка:<b>7.74*861</b> &nbsp;  \"Воздушный стрелок (рабочее)\"  Фантастика  <A HREF=\"/comment/d/demchenko_aw/vs2\">Комментарии: 1003 (18/06/2015)</A> </small><br><DD><font color=\"#555555\">18.06.15. <dd> + Часть 6. Глава 9.</font><DD><small><a href=/img/d/demchenko_aw/vs2/index.shtml>Иллюстрации/приложения: 1 шт.</a></small></DL>";
    private String weakWritingString="<DL><DT><li><A HREF=ch1gl01-02.shtml><b>Глава 1-2</b></A> &nbsp; <b>19k</b> &nbsp; <small> \"@Герой проигранной войны\"  Фэнтези </small><br></DL>";


    private Author author;
    private String pageString;
    private String preparedString;

    @Before
    public void setUp() throws Exception {
        author = new Author();
        author.setLink("http://some.author.link");
        samlibAuthorParser = new SamlibAuthorParserImpl();
        pageString = loadPage("/test_pages/dem_index.html");
        preparedString = samlibAuthorParser.prepareString(pageString);
    }

    @Test
    public void testExtractAuthorName() throws Exception {
        Assert.assertEquals("Демченко А.В.", samlibAuthorParser.parseAuthorName(pageString));
    }

    @Test
    public void testExtractFullWriting(){
        Set<Writing> writings = samlibAuthorParser.parseWritings(fullWritingString);
        Writing writing = writings.iterator().next();
        System.out.println(writing);
        Assert.assertThat(writing,
                Matchers.allOf(
                        Matchers.hasProperty("groupName", Matchers.equalTo("Воздушный стрелок (рабочее)")),
                        Matchers.hasProperty("name", Matchers.equalTo("Воздушный стрелок 2")),
                        Matchers.hasProperty("link", Matchers.equalTo("vs2.shtml")),
                        Matchers.hasProperty("description", Matchers.equalTo("18.06.15.  + Часть 6. Глава 9.")),
                        Matchers.hasProperty("size", Matchers.equalTo("612"))
                )
        );
    }

    @Test
    public void testExtractWeakWriting(){
        Set<Writing> writings = samlibAuthorParser.parseWritings(weakWritingString);
        Writing writing = writings.iterator().next();
        System.out.println(writing);
        Assert.assertThat(writing,
                Matchers.allOf(
                        Matchers.hasProperty("groupName", Matchers.equalTo("Герой проигранной войны")),
                        Matchers.hasProperty("name", Matchers.equalTo("Глава 1-2")),
                        Matchers.hasProperty("link", Matchers.equalTo("ch1gl01-02.shtml")),
                        Matchers.hasProperty("description", Matchers.nullValue()),
                        Matchers.hasProperty("size", Matchers.equalTo("19"))
                )
        );
    }

    @Test
    public void testExtractWritings() throws Exception {
        Set<Writing> writings = samlibAuthorParser.parseWritings(preparedString);
        for(Writing w: writings){
            System.out.println(w);
        }
    }

    private String loadPage(String path) throws IOException {
        return IOUtils.toString(this.getClass().getResourceAsStream(path), Charset.forName("Cp1251"));
    }
}