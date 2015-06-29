package my.wf.samlib.updater.parser.impl;

import my.wf.samlib.exception.PageReadException;
import my.wf.samlib.updater.parser.SamlibPageReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Scope(value = "prototype",  proxyMode = ScopedProxyMode.INTERFACES)
public class SamlibPageReaderImpl implements SamlibPageReader {
    private static  final Logger logger = LoggerFactory.getLogger(SamlibPageReaderImpl.class);
    public static final String DEFAULT_ENCODING = "windows-1251";
    private static final Pattern charsetPattern = Pattern.compile("text/html;\\s+charset=([^\\s]+)\\s*");

    public static final String LINK_SUFFIX = "indextitle.shtml";

    @Override
    public String readPage(String link) {
        logger.debug("read page [{}]", link);
        String pageString;
        try {
            pageString = readPageByLink(link);
        } catch (IOException e) {
            logger.error("can not read link {}" , link);
            throw new PageReadException(e, link);
        }
        logger.debug("downloaded {} symbols", pageString.length());
        return pageString;
    }

    private String readPageByLink(String link) throws IOException {
        logger.debug("read by link [{}]", link);
        URL url = new URL(link);
        URLConnection con = url.openConnection();
        Reader r = new InputStreamReader(con.getInputStream(), getCharset(con.getContentType(), DEFAULT_ENCODING));
        StringBuilder buf = new StringBuilder();
        while (true) {
            int ch = r.read();
            if (ch < 0)
                break;
            buf.append((char) ch);
        }

        return buf.toString();
    }

    protected static String getCharset(String contentType, String encoding){
        if(null == contentType){
            return encoding;
        }
        Matcher m = charsetPattern.matcher(contentType);
        return m.matches() ? m.group(1) : encoding;
    }

    protected String getFullLink(String link){
        return link.endsWith(LINK_SUFFIX)
                ? link.substring(0, link.length() - LINK_SUFFIX.length())
                : link;
    }

}
