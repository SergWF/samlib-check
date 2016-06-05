package my.wf.samlib.integration;

import com.jayway.restassured.RestAssured;
import my.wf.samlib.SamlibWebApplication;
import my.wf.samlib.config.BeanOverridingTestConfig;
import my.wf.samlib.helpers.EntityHelper;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Changed;
import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.service.PropertyViewerService;
import my.wf.samlib.storage.AuthorStorage;
import org.junit.Assert;
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

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(
        classes = {
                SamlibWebApplication.class,
                BeanOverridingTestConfig.class/*should be last in the list*/
        }
)
@WebIntegrationTest(randomPort = true)
@TestPropertySource({"classpath:integration-test.properties"})
public class AuthorsTest {

    @Value("http://localhost:${local.server.port}")
    private String baseUrl;

    @Autowired
    private PropertyViewerService propertyViewerService;
    @Autowired
    private AuthorStorage authorStorage;
    private Author author1;
    private Author author2;
    private Author author3;
    Writing w1;
    Writing w2;
    Writing w3;
    Writing w4;
    Writing w5;
    Writing w6;
    Writing w7;

    @Before
    public void setUp() throws IOException {
        SamlibWebApplication.printProperties(propertyViewerService);
        author1 = EntityHelper.createAuthor("http://a1", "a1");
        w1 = EntityHelper.createWriting("w1", author1);
        w2 = EntityHelper.createWriting("w2", author1);
        w2.setUnread(true);
        w2.getChangesIn().add(Changed.NEW);
        author2 = EntityHelper.createAuthor("http://a2", "a2");
        w3 = EntityHelper.createWriting("w3", author2);
        w4 = EntityHelper.createWriting("w4", author2);
        author3 = EntityHelper.createAuthor("http://a3", "a3");
        w5 = EntityHelper.createWriting("w5", author3);
        w5.setUnread(true);
        w5.getChangesIn().addAll(Arrays.asList(Changed.NAME, Changed.SIZE));
        w6 = EntityHelper.createWriting("w6", author3);
        w6.setUnread(true);
        w6.getChangesIn().addAll(Arrays.asList(Changed.NAME, Changed.SIZE, Changed.DESCRIPTION));
        w7 = EntityHelper.createWriting("w7", author3);
        Set<Author> all = authorStorage.getAll();
        for(Author author: all){
            authorStorage.delete(author);
        }
        author1 = authorStorage.save(author1);
        author2 = authorStorage.save(author2);
        author3 = authorStorage.save(author3);
    }


    @Test
    public void testConfiguration(){
        Assert.assertEquals("prop. values should be read from test/resources/rest-test.properties", "build/samlib-storage-test.data", propertyViewerService.getStorageFile());
    }

    @Test
    public void testGetSingleAuthor(){
        RestAssured
                .when()
                .get(baseUrl + "/authors/1")
                .then()
                .log().everything(true)
                .statusCode(HttpStatus.OK.value())
                .log().everything(true);
    }

    @Test
    public void testGetAuthorList(){
        RestAssured
                .when()
                .get(baseUrl + "/authors")
                .then()
                .log().everything(true)
                .statusCode(HttpStatus.OK.value())
                .log().everything(true);
    }

}
