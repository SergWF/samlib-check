package my.wf.samlib;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;

@Configuration
@ComponentScan(basePackages = {"my.wf.samlib.helpers"})
@PropertySource("classpath:integration-test.properties")
public class TestConfig {

    @PostConstruct
    public void postConstruct(){
        System.out.println("CALL " + this.getClass().getSimpleName());
    }


}
