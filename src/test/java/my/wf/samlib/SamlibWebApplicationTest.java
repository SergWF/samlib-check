package my.wf.samlib;

import my.wf.samlib.config.BeanOverridingTestConfig;
import org.junit.Test;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.TestPropertySource;

@SpringApplicationConfiguration(
        classes = {
                SamlibWebApplication.class,
                BeanOverridingTestConfig.class/*should be last in the list*/
        }
)
@WebIntegrationTest(randomPort = true)
@TestPropertySource({"classpath:integration-test.properties"})
public class SamlibWebApplicationTest {

    @Test
    public void testConfiguration(){

    }

}