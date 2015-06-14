package my.wf.samlib.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = {"my.wf.samlib.service"})
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = "my.wf.samlib.model.repositoriy")
@EntityScan(basePackages = "my.wf.samlib.model.entity")
public class MainConfig {

}
