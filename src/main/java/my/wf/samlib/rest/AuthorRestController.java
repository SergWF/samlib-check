package my.wf.samlib.rest;

import com.wordnik.swagger.annotations.ApiOperation;
import my.wf.samlib.model.dto.NewAuthorDto;
import my.wf.samlib.model.dto.StatisticDto;
import my.wf.samlib.model.dto.UpdatingProcessDto;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.rest.hateoas.HateoasResourceBuilder;
import my.wf.samlib.service.AuthorService;
import my.wf.samlib.service.CustomerService;
import my.wf.samlib.service.SamlibService;
import my.wf.samlib.updater.UpdateRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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
    UpdateRunner updateRunner;


    @ApiOperation(value = "Returns list of all subscribed Authors")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Resources<Resource<Author>> listAuthors(){
        logger.info("GET AUTHORS LIST");
        return HateoasResourceBuilder.createResourceList(customerService.getAuthorsList(getActiveCustomer()));
    }

    @ApiOperation(value = "Returns list of unread Authors")
    @RequestMapping(value = "/unread", method = RequestMethod.GET)
    public Resources<Resource<Author>> getUnread(){
        logger.info("GET UNREAD AUTHORS LIST");
        return HateoasResourceBuilder.createResourceList(customerService.getUnreadAuthors(getActiveCustomer()));
    }

    @ApiOperation(value = "Returns Author's data")
    @RequestMapping(value = "/{authorId}", method = RequestMethod.GET)
    @ResponseBody
    public Resource<Author> getDetails(@PathVariable long authorId) {
        logger.info("GET AUTHOR BY ID {}", authorId);
        return HateoasResourceBuilder.createResource(authorService.findAuthor(authorId));
    }

    @ApiOperation(value = "Deletes Author")
    @RequestMapping(value = "/{authorId}", method = RequestMethod.DELETE)
    @ResponseBody
    public Long deleteAuthor(@PathVariable long authorId) {
        logger.info("GET AUTHOR BY ID {}", authorId);
        authorService.delete(authorId);
        return authorId;
    }


    @ApiOperation(value = "Adds new author")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Resource<Author> addAuthor(@RequestBody NewAuthorDto newAuthorDto){
        logger.info("ADD AUTHOR BY URL: " + newAuthorDto.getUrl());
        Author author = authorService.addAuthor(newAuthorDto.getUrl());
        customerService.addAuthor(getActiveCustomer(), author);
        return HateoasResourceBuilder.createResource(author);
    }

    @ApiOperation(value = "Triggers check of author updates")
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    @ResponseBody
    public UpdatingProcessDto checkAuthors(){
        return  updateRunner.runUpdate();
    }


    @ApiOperation(value = "Returns statistic of selected customer")
    @RequestMapping(value = "/statistic", method = RequestMethod.GET)
    @ResponseBody
    public StatisticDto getStatistic(){
        return customerService.getStatistic(getActiveCustomer());
    }

    @ApiOperation(value = "Uses for adding authors in bulk mode")
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    @ResponseBody
    public Integer doImport(@RequestBody List<String> authorLinks){
        return authorService.importAuthors(getActiveCustomer(), authorLinks);
    }

    @ApiOperation(value = "Returns list of Author's links")
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    @ResponseBody
    public Set<String> doExport(){
        return authorService.exportAuthors();
    }

    private Customer getActiveCustomer() {
        return samlibService.getDefaultCustomer();
    }


}
