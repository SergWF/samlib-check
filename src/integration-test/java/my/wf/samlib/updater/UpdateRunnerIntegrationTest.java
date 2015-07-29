package my.wf.samlib.updater;

import my.wf.samlib.TestConfig;
import my.wf.samlib.WebStubConfig;
import my.wf.samlib.config.MainConfig;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.repositoriy.AuthorRepository;
import my.wf.samlib.service.AuthorService;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {WebStubConfig.class, MainConfig.class, TestConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UpdateRunnerIntegrationTest {

    private static final String LINK_1 = "/test_pages/test_page.html";
    private static final String LINK_2 = "/test_pages/test_page2.html";
    private static final String LINK_3 = "/test_pages/test_page_removed_writing.html";
    private static final String LINK_4 = "/test_pages/test_page_new_writing.html";

    private static final long pause = 1000L;

    @Autowired
    UpdateRunner updateRunnerBean;
    @Autowired
    AuthorService authorService;
    @Autowired
    AuthorRepository authorRepository;

    UpdateRunner updateRunner;

    @Before
    public void setUp() throws Exception {
        updateRunner = Mockito.spy(updateRunnerBean);
    }

    @Test
    public void testDoUpdateWithPause() {
        Author a1 = authorService.addAuthor(LINK_1);
        Author a2 = authorService.addAuthor(LINK_2);
        Author a3 = authorService.addAuthor(LINK_3);
        Author a4 = authorService.addAuthor(LINK_4);
        Mockito.doReturn(true).when(updateRunner).isCanUpdate();
        updateRunner.setPauseBetweenAuthors(pause);
        updateRunner.doUpdate(new Date());
        List<Author> sortedAuthors = new ArrayList<>(4);
        sortedAuthors.add(authorService.findAuthorWithWritings(a1.getId()));
        sortedAuthors.add(authorService.findAuthorWithWritings(a2.getId()));
        sortedAuthors.add(authorService.findAuthorWithWritings(a3.getId()));
        sortedAuthors.add(authorService.findAuthorWithWritings(a4.getId()));
        Collections.sort(sortedAuthors, (o1, o2) -> ((null == o1 ||null == o1.getLastChangedDate()) ? new Date(0) : o1.getLastChangedDate()).compareTo((null == o2 || null == o2.getLastChangedDate()) ? new Date(0) : o2.getLastChangedDate()));
        Author prevAuthor = null;
        for(Author author: sortedAuthors){
            if(null != prevAuthor){
                long delta = author.getLastChangedDate().getTime() - prevAuthor.getLastChangedDate().getTime();
                Assert.assertTrue("actual pause: " + delta, delta >= pause);
            }
            prevAuthor = author;
        }
    }

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