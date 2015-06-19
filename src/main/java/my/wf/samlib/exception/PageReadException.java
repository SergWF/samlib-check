package my.wf.samlib.exception;

import java.io.IOException;

public class PageReadException extends SamlibException {
    private String link;
    public PageReadException(IOException e, String link) {
        super();
    }

    public String getLink() {
        return link;
    }
}
