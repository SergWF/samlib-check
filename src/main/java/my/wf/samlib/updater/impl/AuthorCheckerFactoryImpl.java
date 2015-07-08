package my.wf.samlib.updater.impl;

import my.wf.samlib.updater.AuthorCheckerFactory;
import my.wf.samlib.updater.parser.AuthorChecker;
import my.wf.samlib.updater.parser.impl.AuthorCheckerImpl;
import my.wf.samlib.updater.parser.impl.SamlibAuthorParserImpl;
import my.wf.samlib.updater.parser.impl.SamlibPageReaderImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthorCheckerFactoryImpl implements AuthorCheckerFactory {

    @Value("${link.suffix}")
    private String linkSuffix;
    @Value("${ban.check.url}")
    private String banCheckUrl;


    public AuthorChecker getAuthorChecker(){
        AuthorCheckerImpl authorChecker = new AuthorCheckerImpl();
        authorChecker.setSamlibPageReader(new SamlibPageReaderImpl());
        authorChecker.setLinkSuffix(linkSuffix);
        authorChecker.setBanCheckUrl(banCheckUrl);
        authorChecker.setSamlibAuthorParser(new SamlibAuthorParserImpl());
        return authorChecker;
    }
}
