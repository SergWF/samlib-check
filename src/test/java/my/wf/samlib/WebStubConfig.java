package my.wf.samlib;

import my.wf.samlib.helpers.EntityHelper;
import my.wf.samlib.updater.parser.SamlibPageReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.IOException;

@Configuration
public class WebStubConfig {

    @Bean
    @Primary
    public SamlibPageReader samlibPageReader(){
        return new SamlibPageReader() {
            @Override
            public String readPage(String link) {
                String filePath = (link.endsWith("/"))?link.substring(0, link.length() -2):link;
                try {
                    return EntityHelper.loadPage(filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

}
