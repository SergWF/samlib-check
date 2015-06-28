package my.wf.samlib.rest;

import com.wordnik.swagger.annotations.ApiOperation;
import my.wf.samlib.model.dto.AuthorDetailsDto;
import my.wf.samlib.model.dto.builder.AuthorDtoBuilder;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.model.entity.Subscription;
import my.wf.samlib.service.AuthorService;
import my.wf.samlib.service.SamlibService;
import my.wf.samlib.service.SubscriptionService;
import my.wf.samlib.service.UtilsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/author", produces= MediaType.APPLICATION_JSON_VALUE)
public class AuthorRestController {
    private static final Logger logger = LoggerFactory.getLogger(AuthorRestController.class);

    @Autowired
    AuthorService authorService;
    @Autowired
    UtilsService utilsService;
    @Autowired
    SamlibService samlibService;
    @Autowired
    SubscriptionService subscriptionService;


    @ApiOperation(value = "Returns list of all subscribed Authors")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public List<Author> listAuthors(){
        logger.info("GET AUTHORS LIST");
        List<Author> authorsList = authorService.findAllAuthors();
        logger.info("found {} authors", authorsList.size());
        return authorsList;
    }

    @ApiOperation(value = "Returns Author's data")
    @RequestMapping(value = "/{authorId}", method = RequestMethod.GET)
    @ResponseBody
    public AuthorDetailsDto getDetails(@PathVariable long authorId) {
        logger.info("GET AUTHOR BY ID {}", authorId);
        Customer customer = samlibService.getActiveCustomer();
        Subscription subscription = subscriptionService.getSubscriptionByCustomerAndAuthorId(customer.getId(), authorId);
        return AuthorDtoBuilder.buildDto(authorService.findAuthor(authorId),subscription);
    }

//    @ApiOperation(value = "Deletes Author")
//    @RequestMapping(value = "/{authorId}", method = RequestMethod.DELETE)
//    @ResponseBody
//    public Long deleteAuthor(@PathVariable long authorId) {
//        logger.info("GET AUTHOR BY ID {}", authorId);
//        utilsService.delete(authorId);
//        return authorId;
//    }
//
//
//    @ApiOperation(value = "Adds new author")
//    @RequestMapping(method = RequestMethod.POST)
//    @ResponseBody
//    public Resource<Author> addAuthor(@RequestBody NewAuthorDto newAuthorDto){
//        logger.info("ADD AUTHOR BY URL: " + newAuthorDto.getUrl());
//        Author author = utilsService.addAuthor(newAuthorDto.getUrl());
//        customerService.addAuthor(getActiveCustomer(), author);
//        return HateoasResourceBuilder.createResource(author);
//    }



    private Customer getActiveCustomer() {
        return samlibService.getActiveCustomer();
    }


}
