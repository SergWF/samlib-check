package my.wf.samlib.rest;

import my.wf.samlib.hateoas.HateoasResourceBuilder;
import my.wf.samlib.model.dto.NewAuthorDto;
import my.wf.samlib.model.dto.UpdatingProcessDto;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.service.AuthorService;
import my.wf.samlib.service.CustomerService;
import my.wf.samlib.service.SamlibService;
import my.wf.samlib.updater.AuthorUpdater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/author", produces= MediaType.APPLICATION_JSON_VALUE)
public class AuthorRestController {
    private static final Logger logger = LoggerFactory.getLogger(AuthorRestController.class);

    @Autowired
    AuthorService authorService;
    @Autowired
    CustomerService customerService;
    @Autowired
    SamlibService samlibService;
    @Autowired
    AuthorUpdater updater;


    @RequestMapping("/list")
    @ResponseBody
    public Resources<Resource<Author>> listAuthors(){
        logger.info("GET AUTHORS LIST");
        return HateoasResourceBuilder.createResourceList(customerService.getAuthorsList(getActiveCustomer()));
    }

    @RequestMapping(value = "/unread", method = RequestMethod.GET)
    public Resources<Resource<Author>> getUnread(){
        logger.info("GET UNREAD AUTHORS LIST");
        return HateoasResourceBuilder.createResourceList(customerService.getUnreadAuthors(getActiveCustomer()));
    }

    @RequestMapping(value = "/{authorId}")
    @ResponseBody
    public Resource<Author> getDetails(@PathVariable long authorId) {
        logger.info("GET AUTHOR BY ID {}", authorId);
        return HateoasResourceBuilder.createResource(authorService.findAuthor(authorId));
    }


    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Resource<Author> addAuthor(@RequestBody NewAuthorDto newAuthorDto){
        logger.info("ADD AUTHOR BY URL: " + newAuthorDto.getUrl());
        Author author = authorService.addAuthor(newAuthorDto.getUrl());
        customerService.addAuthor(getActiveCustomer(), author);
        return HateoasResourceBuilder.createResource(author);
    }

    @RequestMapping(value = "/update")
    @ResponseBody
    public UpdatingProcessDto updateAuthors(){
        return  updater.updateAll();
    }



    private Customer getActiveCustomer() {
        return samlibService.getDefaultCustomer();
    }


}
