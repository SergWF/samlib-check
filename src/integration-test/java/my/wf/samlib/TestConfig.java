package my.wf.samlib;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

@Configuration
@ComponentScan(basePackages = {"my.wf.samlib.helpers"})
//@ActiveProfiles("test")
@TestPropertySource(locations="classpath:test.properties")
public class TestConfig {


}
