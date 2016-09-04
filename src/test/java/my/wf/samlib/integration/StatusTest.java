package my.wf.samlib.integration;

import com.jayway.restassured.RestAssured;
import my.wf.samlib.SamlibWebApplication;
import my.wf.samlib.config.BeanOverridingTestConfig;
import my.wf.samlib.helpers.InitHelper;
import my.wf.samlib.storage.AuthorStorage;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
public class StatusTest {

    @Value("http://localhost:${local.server.port}")
    private String baseUrl;
    @Autowired
    private AuthorStorage authorStorage;

    @Before
    public void setUp() throws Exception {
        InitHelper.initAuthors(authorStorage);

    }

    @Test
    public void testGetStatusNoUpdates(){
        RestAssured
                .when()
                .get(baseUrl + "/utils/statistic")
                .then()
                .log().everything(true)
                .statusCode(HttpStatus.OK.value())
                .body("systemTime", Matchers.notNullValue())
                .body("lastUpdated", Matchers.notNullValue())
                .body("updating", Matchers.nullValue());
    }

    @Test
    public void testGetStatusDuringUpdate(){
        RestAssured
                .when()
                .get(baseUrl + "/utils/statistic")
                .then()
                .log().everything(true)
                .statusCode(HttpStatus.OK.value())
                .log().everything(true)
                .body("currentTime", Matchers.notNullValue())
                .body("lastUpdated", Matchers.notNullValue())
                .body("updating", Matchers.notNullValue())
                .body("updating.startTime", Matchers.notNullValue())
                .body("updating.totalAuthors", Matchers.notNullValue())
                .body("updating.processedAuthors", Matchers.notNullValue())
        ;

    }

}
