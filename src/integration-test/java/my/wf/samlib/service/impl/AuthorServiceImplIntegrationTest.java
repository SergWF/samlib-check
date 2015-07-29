package my.wf.samlib.service.impl;

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
import my.wf.samlib.TestConfig;
import my.wf.samlib.helpers.InitHelper;

import java.util.Map;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {MainConfig.class, TestConfig.class})
public class AuthorServiceImplIntegrationTest {

    @Autowired
    private AuthorService authorService;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private InitHelper initHelper;

    @Test
    public void testCreateAuthorOnAdd() throws Exception {
        Author author = authorService.addAuthor("http://" + UUID.randomUUID().toString());
        Assert.assertThat(author,
                Matchers.allOf(
                        Matchers.hasProperty("id", Matchers.notNullValue()),
                        Matchers.equalTo(authorRepository.findOne(author.getId()))
                )
        );
    }

    @Test
    public void testFindAuthorOnAdd() throws Exception {
        Map<Integer, Author> authors = initHelper.initAuthors(3);
        Long id = authors.get(0).getId();
        String link = authors.get(0).getLink();

        Author author = authorService.addAuthor(link);
        Assert.assertThat(author, Matchers.hasProperty("id", Matchers.equalTo(id)));
    }

    @Test
    public void testFindAuthor() throws Exception {
        Map<Integer, Author> authors = initHelper.initAuthors(3);
        Long authorId = authors.get(1).getId();
        Author author = authorService.findAuthor(authorId);
        Assert.assertEquals(authorId, author.getId());
    }
}