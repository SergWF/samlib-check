package my.wf.samlib;

import my.wf.samlib.service.SamlibService;
import my.wf.samlib.service.UtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableScheduling
@EnableAutoConfiguration
@PropertySources({
        @PropertySource("classpath:default.properties"),
        @PropertySource(value = "file:${external.config}", ignoreResourceNotFound = true)
})
public class SamlibWebApplication {

    @Autowired
    SamlibService samlibService;
    @Autowired
    UtilsService utilsService;

    public static void main(String[] args) {
        SpringApplication.run(SamlibWebApplication.class, args);

    }


}
