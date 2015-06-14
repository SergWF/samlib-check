package my.wf.samlib.updater.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SamlibPageReader {
    private static  final Logger logger = LoggerFactory.getLogger(SamlibPageReader.class);
    public static final String DEFAULT_ENCODING = "windows-1251";
    private static final Pattern charsetPattern = Pattern.compile("text/html;\\s+charset=([^\\s]+)\\s*");

    public String readPage(String link) throws IOException {
        logger.debug("read page [{}]", link);
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
        logger.debug("downloaded {} bytes", buf.length());
        return buf.toString();
    }

    public static String getCharset(String contentType, String encoding){
        if(null == contentType){
            return encoding;
        }
        Matcher m = charsetPattern.matcher(contentType);
        return m.matches() ? m.group(1) : encoding;
    }

}
