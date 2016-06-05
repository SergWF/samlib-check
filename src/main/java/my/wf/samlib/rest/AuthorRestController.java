package my.wf.samlib.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/author", produces= MediaType.APPLICATION_JSON_VALUE)
public class AuthorRestController {
    private static final Logger logger = LoggerFactory.getLogger(AuthorRestController.class);

//    @Autowired
//    AuthorService authorService;
//
//
//
//    @ApiOperation(value = "Returns list of all subscribed Authors")
//    @RequestMapping(value = "/list", method = RequestMethod.GET)
//    @ResponseBody
//    public Set<Author> listAuthors(){
//        logger.info("GET AUTHORS LIST");
//        Set<Author> authorsList = authorService.findAllAuthors();
//        logger.debug("found {} authors", authorsList.size());
//        return authorsList;
//    }
//
//    @ApiOperation(value = "Returns Author's data")
//    @RequestMapping(value = "/{authorId}", method = RequestMethod.GET)
//    @ResponseBody
//    public Author getDetails(@PathVariable long authorId) {
//        logger.info("GET AUTHOR BY ID {}", authorId);
//        return authorService.findAuthor(authorId);
//    }



}
