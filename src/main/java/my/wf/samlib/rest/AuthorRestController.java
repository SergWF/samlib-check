package my.wf.samlib.rest;

import my.wf.samlib.model.dto.NewAuthorDto;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.service.AuthorService;
import my.wf.samlib.service.CustomerService;
import my.wf.samlib.service.SamlibService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/author")
public class AuthorRestController {
    private static final Logger logger = LoggerFactory.getLogger(AuthorRestController.class);

    @Autowired
    AuthorService authorService;
    @Autowired
    CustomerService customerService;
    @Autowired
    SamlibService samlibService;


    @RequestMapping("/list")
    @ResponseBody
    public List<Author> listAuthors(){
        logger.info("GET AUTHORS LIST");
        return customerService.getAuthorsList(getActiveCustomer());
    }

    @RequestMapping(value = "/unread", method = RequestMethod.GET)
    public List<Author> getUnread(){
        logger.info("GET UNREAD AUTHORS LIST");
        return customerService.getUnreadAuthors(getActiveCustomer());
    }

    @RequestMapping("/{authorId}")
    @ResponseBody
    public Author getDetails(@PathVariable long authorId){
        logger.info("GET AUTHOR BY ID {}", authorId);
        return authorService.findAuthor(authorId);
    }


    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Author addAuthor(@RequestBody NewAuthorDto newAuthorDto){
        logger.info("ADD AUTHOR BY URL: " + newAuthorDto.getUrl());
        Author author = authorService.addAuthor(newAuthorDto.getUrl());
        customerService.addAuthor(getActiveCustomer(), author);
        return author;
    }



    private Customer getActiveCustomer() {
        return samlibService.getDefaultCustomer();
    }


}
