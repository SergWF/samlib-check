package my.wf.samlib.exception;

import java.io.IOException;

public class PageReadException extends SamlibException {
    private String link;
    public PageReadException(IOException e, String link) {
        super("Problems with link " + link);
    }

    public String getLink() {
        return link;
    }
}
