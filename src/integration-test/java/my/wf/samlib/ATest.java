package my.wf.samlib;

import my.wf.samlib.config.MainConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {MainConfig.class, TestConfig.class})
//@TestPropertySource("classpath:integration-test.properties")
public class ATest {


    @Value("${probe.value}")
    String probeValue;

    @Test
    public void test(){
        Assert.assertEquals("EXPECTED testval", "testval", probeValue);
    }
}
