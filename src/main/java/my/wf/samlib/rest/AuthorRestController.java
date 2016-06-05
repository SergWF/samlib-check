package my.wf.samlib.rest;

import io.swagger.annotations.ApiOperation;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.service.AuthorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping(value = "/author", produces= MediaType.APPLICATION_JSON_VALUE)
public class AuthorRestController {
    private static final Logger logger = LoggerFactory.getLogger(AuthorRestController.class);

    @Autowired
    AuthorService authorService;



    @ApiOperation(value = "Returns list of all subscribed Authors")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Set<Author> listAuthors(){
        logger.info("GET AUTHORS LIST");
        Set<Author> authorsList = authorService.findAllAuthors();
        logger.debug("found {} authors", authorsList.size());
        return authorsList;
    }

    @ApiOperation(value = "Returns Author's data")
    @RequestMapping(value = "/{authorId}", method = RequestMethod.GET)
    @ResponseBody
    public Author getDetails(@PathVariable long authorId) {
        logger.info("GET AUTHOR BY ID {}", authorId);
        return authorService.findAuthor(authorId);
    }

    @ApiOperation(value = "Deletes Author")
    @RequestMapping(value = "/{authorId}", method = RequestMethod.DELETE)
    @ResponseBody
    public Long deleteAuthor(@PathVariable long authorId) throws IOException {
        logger.info("DELETE AUTHOR BY ID {}", authorId);
        authorService.delete(authorId);
        return authorId;
    }

}
