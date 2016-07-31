package my.wf.samlib.integration;

import com.jayway.restassured.RestAssured;
import my.wf.samlib.SamlibWebApplication;
import my.wf.samlib.config.BeanOverridingTestConfig;
import my.wf.samlib.helpers.EntityHelper;
import my.wf.samlib.helpers.InitHelper;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Changed;
import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.service.PropertyViewerService;
import my.wf.samlib.storage.AuthorStorage;
import org.hamcrest.Matchers;
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
import java.util.stream.Stream;

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

    @Before
    public void setUp() throws IOException {
        SamlibWebApplication.printProperties(propertyViewerService);
        InitHelper.initAuthors(authorStorage);
    }


    @Test
    public void testConfiguration(){
        Assert.assertEquals("prop. values should be read from test/resources/rest-test.properties", "build/samlib-storage-test.json", propertyViewerService.getStorageFile());
    }

    @Test
    public void testGetSingleAuthor(){
        RestAssured
                .when()
                .get(baseUrl + "/authors/1")
                .then()
                .log().everything(true)
                .statusCode(HttpStatus.OK.value())
                .body("link", Matchers.equalTo("http://a1/"))
                .body("name", Matchers.equalTo("a1"))
                .body("writings.size()", Matchers.is(2))
                .body("unread", Matchers.is(1))
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
                .body("size()", Matchers.is(3))
                .log().everything(true);
    }

    @Test
    public void testAddAuthor(){
        String authorUrl = "http://a4/";
        RestAssured
                .given()
                .body(authorUrl)
                .post(baseUrl + "/authors")
                .then()
                .log().everything(true)
                .statusCode(HttpStatus.CREATED.value())
                .body("id", Matchers.notNullValue())
                .body("link", Matchers.equalTo(authorUrl))
                .body("name", Matchers.equalTo("author " + authorUrl))
                .log().everything(true);
    }

    @Test
    public void testDeleteAuthor(){
        RestAssured
                .when()
                .delete(baseUrl + "/authors/1")
                .then()
                .log().everything(true)
                .statusCode(HttpStatus.OK.value())
                .log().everything(true);
    }

    @Test
    public void testMarkAllUnread(){
        Long authorId = 1L;
        Assert.assertEquals(1, authorStorage.getById(authorId).getUnread());

        RestAssured
                .when()
                .delete(baseUrl + "/authors/"+authorId+"/unread")
                .then()
                .log().everything(true)
                .statusCode(HttpStatus.OK.value())
                .log().everything(true);

        Assert.assertEquals(0, authorStorage.getById(authorId).getUnread());
    }

    @Test
    public void testAddWritingToUnreadList(){
        Long authorId = 3L;
        String writingLink ="w5.shtml";

        Writing writing = authorStorage.getById(authorId)
                .getWritings()
                .stream()
                .filter((w) -> w.getLink()
                        .equals(writingLink)).findFirst().get();
        //Pre-test check if writing really exists and it is marked as Unread
        Assert.assertTrue(writing.isUnread());
        Assert.assertNotNull(writing);
        writing.setUnread(false);
        authorStorage.save(writing.getAuthor());

        RestAssured
                .when()
                .delete(baseUrl + "/authors/"+authorId+"/unread/" + writingLink)
                .then()
                .log().everything(true)
                .statusCode(HttpStatus.OK.value())
                .log().everything(true);

        writing = authorStorage.getById(authorId)
                .getWritings()
                .stream()
                .filter((w) -> w.getLink()
                        .equals(writingLink)).findFirst().get();
        Assert.assertFalse(writing.isUnread());
    }

    @Test
    public void testRemoveWritingFromUnreadList(){
        Long authorId = 3L;
        String writingLink ="w5.shtml";

        Writing writing = authorStorage.getById(authorId)
                .getWritings()
                .stream()
                .filter((w) -> w.getLink()
                        .equals(writingLink)).findFirst().get();
        //Pre-test check if writing really exists
        Assert.assertNotNull(writing);
        writing.setUnread(true);
        authorStorage.save(writing.getAuthor());

        RestAssured
                .when()
                .put(baseUrl + "/authors/"+authorId+"/unread/" + writingLink)
                .then()
                .log().everything(true)
                .statusCode(HttpStatus.OK.value())
                .log().everything(true);

        writing = authorStorage.getById(authorId)
                .getWritings()
                .stream()
                .filter((w) -> w.getLink()
                        .equals(writingLink)).findFirst().get();
        Assert.assertTrue(writing.isUnread());
    }



}
