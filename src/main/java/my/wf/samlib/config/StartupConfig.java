package my.wf.samlib.config;

import my.wf.samlib.storage.AuthorStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
public class StartupConfig {

    @Autowired
    private AuthorStorage authorStorage;

    @PostConstruct
    public void afterStartup() throws IOException {
        authorStorage.loadFromPhysicalStorage();
    }

}
