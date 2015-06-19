package my.wf.samlib.updater.parser;

import my.wf.samlib.model.entity.Author;

public interface SamlibAuthorParser {
    Author parse(String authorLink, String page);
}
