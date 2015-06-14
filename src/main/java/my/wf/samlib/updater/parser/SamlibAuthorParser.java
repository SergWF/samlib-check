package my.wf.samlib.updater.parser;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Writing;

import java.util.ArrayList;
import java.util.List;

public class SamlibAuthorParser {

    public Author parse(String link, String page){
        Author author = new Author();
        author.setLink(link);
        author.setName(extractAuthorName(page));
        author.getWritings().addAll(extractWritings(page));
        return author;
    }

    protected String extractAuthorName(String page) {
        return null;
    }

    protected List<Writing> extractWritings(String page) {
        List<Writing> writings = new ArrayList<>();
        return writings;
    }
}
