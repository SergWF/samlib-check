package my.wf.samlib.service.impl;


import my.wf.samlib.helpers.EntityHelper;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.repositoriy.AuthorRepository;
import my.wf.samlib.service.UtilsService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

public class AuthorServiceTest {
    private static final String AUTHOR_URL = "http://1/";
    public static final String NEW_AUTHOR_URL_NO_SLASH = "http://new_author";
    public static final String NEW_AUTHOR_URL_SLASH = "http://new_author/";
    private Author author;

    @Spy
    @InjectMocks
    AuthorServiceImpl authorService;
    @Mock
    AuthorRepository authorRepository;
    @Mock
    UtilsService utilsService;


    @Before
    public void setUp() throws Exception {
        author = EntityHelper.createAuthor(AUTHOR_URL, "Existing Author");
        author.setId(1L);
        MockitoAnnotations.initMocks(this);
        authorService.setLinkSuffix("suffix");
        Mockito.reset(authorService);
        Mockito.doReturn(author).when(authorRepository).findByLink(AUTHOR_URL);
    }

    @Test
    public void testAddExistingAuthor(){
        Assert.assertEquals(author, authorService.addAuthor(AUTHOR_URL));
        Mockito.verify(authorRepository, Mockito.never()).save(Mockito.any(Author.class));
    }
    @Test
    public void testAddExistingAuthorUrlNoSlash(){
        String authorUrl = AUTHOR_URL.substring(0, AUTHOR_URL.length()  -1);
        Assert.assertEquals(AUTHOR_URL, authorUrl + "/");
        Assert.assertEquals(author, authorService.addAuthor(authorUrl));
        Mockito.verify(authorRepository, Mockito.never()).save(Mockito.any(Author.class));
    }
}
