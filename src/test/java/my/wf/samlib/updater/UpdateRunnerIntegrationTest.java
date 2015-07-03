package my.wf.samlib.updater;

import my.wf.samlib.TestConfig;
import my.wf.samlib.WebStubConfig;
import my.wf.samlib.config.MainConfig;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.repositoriy.AuthorRepository;
import my.wf.samlib.service.AuthorService;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {WebStubConfig.class, MainConfig.class, TestConfig.class})
public class UpdateRunnerIntegrationTest {

    private static final String LINK_1 = "/test_pages/test_page.html";
    private static final String LINK_2 = "/test_pages/test_page2.html";

    @Autowired
    UpdateRunner updateRunner;
    @Autowired
    AuthorService authorService;
    @Autowired
    AuthorRepository authorRepository;


    @Test
    public void testDoUpdateNewAuthor() {
        Author a1 = authorService.addAuthor(LINK_1);
        updateRunner.doUpdateAuthor(a1, new Date());
        Assert.assertThat(authorService.findAuthorWithWritings(a1.getId()),
                Matchers.allOf(
                        Matchers.hasProperty("link", Matchers.equalTo(LINK_1)),
                        Matchers.hasProperty("writings", Matchers.hasSize(4)),
                        Matchers.hasProperty("writings",
                                Matchers.hasItems(
                                        Matchers.hasProperty("link", Matchers.equalTo("multisectionpagesection1section2.shtml")),
                                        Matchers.hasProperty("link", Matchers.equalTo("page1.shtml")),
                                        Matchers.hasProperty("link", Matchers.equalTo("page2.shtml")),
                                        Matchers.hasProperty("link", Matchers.equalTo("page3.shtml"))
                                )
                        )

                )
        );
    }

    @Test
    public void testDoUpdateExistingAuthor() {
        Author a1 = authorService.addAuthor(LINK_1);
        updateRunner.doUpdateAuthor(a1, new Date());
        Assert.assertThat(authorService.findAuthorWithWritings(a1.getId()),
                Matchers.allOf(
                        Matchers.hasProperty("link", Matchers.equalTo(LINK_1)),
                        Matchers.hasProperty("writings", Matchers.hasSize(4)),
                        Matchers.hasProperty("writings",
                                Matchers.hasItems(
                                        Matchers.hasProperty("link", Matchers.equalTo("multisectionpagesection1section2.shtml")),
                                        Matchers.hasProperty("link", Matchers.equalTo("page1.shtml")),
                                        Matchers.hasProperty("link", Matchers.equalTo("page2.shtml")),
                                        Matchers.hasProperty("link", Matchers.equalTo("page3.shtml"))
                                )
                        )

                )
        );
        a1.setLink(LINK_2);
        authorRepository.save(a1);
        updateRunner.doUpdateAuthor(a1, new Date());
        Assert.assertThat(authorService.findAuthorWithWritings(a1.getId()),
                Matchers.allOf(
                        Matchers.hasProperty("link", Matchers.equalTo(LINK_2)),
                        Matchers.hasProperty("writings", Matchers.hasSize(4)),
                        Matchers.hasProperty("writings",
                                Matchers.hasItems(
                                        Matchers.hasProperty("link", Matchers.equalTo("another_page1.shtml")),
                                        Matchers.hasProperty("link", Matchers.equalTo("another_page2.shtml")),
                                        Matchers.hasProperty("link", Matchers.equalTo("another_page3.shtml")),
                                        Matchers.hasProperty("link", Matchers.equalTo("another_page4.shtml"))
                                )
                        )

                )
        );


    }

}