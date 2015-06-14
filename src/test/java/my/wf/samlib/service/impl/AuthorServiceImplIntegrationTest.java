package my.wf.samlib.service.impl;

import my.wf.samlib.TestConfig;
import my.wf.samlib.config.MainConfig;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.repositoriy.AuthorRepository;
import my.wf.samlib.service.AuthorService;
import my.wf.samlib.service.SamlibService;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {MainConfig.class, TestConfig.class})
public class AuthorServiceImplIntegrationTest {

    @Autowired
    AuthorService authorService;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    SamlibService samlibService;

    @Test
    @Transactional
    public void testCreateAuthor() throws Exception {
        Author author = authorService.addAuthor("http://1");
        Assert.assertThat(author,
                Matchers.allOf(
                        Matchers.hasProperty("id", Matchers.notNullValue()),
                        Matchers.equalTo(authorRepository.findOne(author.getId()))
                )
        );
    }



    @Test
    public void testFindAuthor() throws Exception {

    }
}