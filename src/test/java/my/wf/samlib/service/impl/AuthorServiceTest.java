package my.wf.samlib.service.impl;


import my.wf.samlib.model.entity.Author;
import my.wf.samlib.storage.AuthorStorage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class AuthorServiceTest {
    private static final String AUTHOR_URL = "http://1/";
    public static final String NEW_AUTHOR_LINK = "http://new_author/";
    public static final String EXISING_AUTHOR_LINK = "http://existing_author/";

    @Mock
    private Author author;

    @Spy
    @InjectMocks
    private AuthorServiceImpl authorService;
    @Mock
    private AuthorStorage authorStorage;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        authorService.setLinkSuffix("");
        Mockito.doReturn(1L).when(author).getId();
        Mockito.doReturn(NEW_AUTHOR_LINK).when(author).getLink();
    }

    @Test
    public void testAddNewAuthor() throws Exception {
        Mockito.doReturn(null).when(authorStorage).findByLink(Mockito.eq(NEW_AUTHOR_LINK));
        Mockito.doReturn(author).when(authorService).createAndSaveNewAuthor(Mockito.eq(NEW_AUTHOR_LINK));
        authorService.addAuthor(NEW_AUTHOR_LINK);
        Mockito.verify(authorService).createAndSaveNewAuthor(Mockito.eq(NEW_AUTHOR_LINK));
    }

    @Test
    public void testAddExistingAuthor() throws Exception {
        Mockito.doReturn(author).when(authorStorage).findByLink(Mockito.eq(EXISING_AUTHOR_LINK));
        authorService.addAuthor(EXISING_AUTHOR_LINK);
        Mockito.verify(authorService, Mockito.never()).createAndSaveNewAuthor(Mockito.eq(EXISING_AUTHOR_LINK));
    }
}
