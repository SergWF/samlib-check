package my.wf.samlib;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@ComponentScan(basePackages = {"my.wf.samlib.helpers"})
@ActiveProfiles("test")
public class TestConfig {
}
