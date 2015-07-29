package my.wf.samlib;

import my.wf.samlib.exception.PageReadException;
import my.wf.samlib.updater.AuthorCheckerFactory;
import my.wf.samlib.updater.parser.AuthorChecker;
import my.wf.samlib.updater.parser.SamlibPageReader;
import my.wf.samlib.updater.parser.impl.AuthorCheckerImpl;
import my.wf.samlib.updater.parser.impl.SamlibAuthorParserImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import my.wf.samlib.helpers.EntityHelper;

import java.io.IOException;

@Configuration
public class WebStubConfig {

    @Bean
    @Primary
    public AuthorCheckerFactory authorCheckerFactory(){
        return new AuthorCheckerFactory() {
            @Override
            public AuthorChecker getAuthorChecker() {
                AuthorCheckerImpl authorChecker = new AuthorCheckerImpl();
                authorChecker.setSamlibPageReader(createSamlibPageReader());
                authorChecker.setSamlibAuthorParser(new SamlibAuthorParserImpl());
                return authorChecker;
            }
        };
    }


    private SamlibPageReader createSamlibPageReader(){
        return new SamlibPageReader() {

            @Override
            public String aa() {
                return "FILE";
            }

            @Override
            public String readPage(String link) {
                String filePath = (link.endsWith("/"))?link.substring(0, link.length() -2):link;
                try {
                    return EntityHelper.loadPage(filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new PageReadException(e, link);
                }
            }
        };
    }

}
