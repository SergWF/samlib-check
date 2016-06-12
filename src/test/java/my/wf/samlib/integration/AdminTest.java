package my.wf.samlib.integration;

import com.jayway.restassured.RestAssured;
import my.wf.samlib.SamlibWebApplication;
import my.wf.samlib.config.BeanOverridingTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(
        classes = {
                SamlibWebApplication.class,
                BeanOverridingTestConfig.class/*should be last in the list*/
        }
)
@WebIntegrationTest(randomPort = true)
@TestPropertySource({"classpath:integration-test.properties"})
public class AdminTest {


    @Value("http://localhost:${local.server.port}")
    private String baseUrl;

    @Test
    public void testExport(){
        RestAssured
                .when()
                .get(baseUrl + "/admin/export")
                .then()
                .log().everything(true)
                .statusCode(HttpStatus.OK.value())
                .log().everything(true);
    }

    @Test
    public void testImport(){

    }
}
