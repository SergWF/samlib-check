package my.wf.samlib.integration;

import com.jayway.restassured.RestAssured;
import my.wf.samlib.SamlibWebApplication;
import my.wf.samlib.config.BeanOverridingTestConfig;
import my.wf.samlib.helpers.EntityHelper;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.storage.AuthorStorage;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.time.LocalDateTime;

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

    private static final Logger logger = LoggerFactory.getLogger(AdminTest.class);

    private static final LocalDateTime DATE0 = LocalDateTime.of(2010, 10, 24, 10, 0, 0);
    private static final LocalDateTime DATE1 = LocalDateTime.of(2011, 10, 24, 10, 0, 0);
    private static final LocalDateTime DATE2 = LocalDateTime.of(2012, 10, 24, 10, 0, 0);
    private static final LocalDateTime DATE3 = LocalDateTime.of(2013, 10, 24, 10, 0, 0);
    private static final LocalDateTime TEST_DATE = LocalDateTime.of(2012, 11, 24, 10, 0, 0);
    private static final String AUTHOR1_LINK = "http://a1/";
    private static final String AUTHOR2_LINK = "http://a2/";
    private static final String AUTHOR3_LINK = "http://a3/";


    @Value("http://localhost:${local.server.port}")
    private String baseUrl;


    @Autowired
    private AuthorStorage authorStorage;

    private Author author1;
    private Author author2;
    private Author author3;

    @Before
    public void setUp() throws Exception {
        author1 = EntityHelper.createAuthor(AUTHOR1_LINK, "a1");
        author2 = EntityHelper.createAuthor(AUTHOR2_LINK, "a2");
        author3 = EntityHelper.createAuthor(AUTHOR3_LINK, "a3");
        authorStorage.save(author1);
        authorStorage.save(author2);
        authorStorage.save(author3);
    }

    @Test
    public void testExport(){
        RestAssured
                .when()
                .get(baseUrl + "/admin/export")
                .then()
                .log().everything(true)
                .statusCode(HttpStatus.OK.value())
                .contentType("application/octet-stream;charset=UTF-8")
//                .content("filename", Matchers.equalTo("export-" + new SimpleDateFormat("yyyyMMddHHmm").format(new Date())+ "json"))
                .log().everything(true);
    }

    @Test
    public void testImport(){
        String importFileName = this.getClass().getClassLoader().getResource("data/import.json").getFile();
        File importFile = new File(importFileName);
        RestAssured
                .given()
                .multiPart(importFile)
                .post(baseUrl + "/admin/import")
                .then()
                .log().everything(true)
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo("3"))
                .log().everything(true);
    }
}
