package my.wf.samlib.rest;

import io.swagger.annotations.ApiOperation;
import my.wf.samlib.model.dto.AuthorItemListDto;
import my.wf.samlib.model.dto.builder.AuthorDtoBuilder;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.service.AuthorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping(value = "/authors", produces= MediaType.APPLICATION_JSON_VALUE)
public class SubscriptionRestController {

    private static final Logger logger  = LoggerFactory.getLogger(SubscriptionRestController.class);

    @Autowired
    AuthorService authorService;



    @ApiOperation(value = "Creates a new subscription")
    @RequestMapping(value = "/subscribe/url", method = RequestMethod.POST)
    public Author subscribe(@RequestBody String authorUrl) throws IOException {
        return authorService.addAuthor(authorUrl);
    }

    @ApiOperation(value = "Returns list of all subscribed Authors")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Set<AuthorItemListDto> getAuthorList(){
        return AuthorDtoBuilder.createList(authorService.findAllAuthors());
    }

    @ApiOperation(value = "Returns subscription data")
    @RequestMapping(value = "/{authorId}", method = RequestMethod.GET)
    public Author getSubscriptionById(@PathVariable(value = "authorId") long authorId){
        return authorService.findAuthor(authorId);
    }

    @ApiOperation(value = "Mark all author writings as read")
    @RequestMapping(value = "/{authorId}/unread/all", method = RequestMethod.DELETE)
    public AuthorItemListDto markAllAsRead(@PathVariable(value = "authorId") Long authorId){
        Author author = authorService.findAuthor(authorId);
        authorService.markAllWritingsRead(author);
        return AuthorDtoBuilder.createDto(authorService.findAuthor(authorId));
    }

    @ApiOperation(value = "mark writing as read")
    @RequestMapping(value = "/{authorId}/unread/", method = RequestMethod.DELETE)
    public Author removeFromUnreadList(@PathVariable(value = "authorId") Long authorId, @RequestParam(value = "writingLink") String writingLink) throws IOException {
        logger.info("Remove from UnreadList, authorId={}, writing.link={}",authorId, writingLink);
        return changeWritingUnreadFlag(authorId, writingLink, false);
    }

    @ApiOperation(value = "Add writing to unread list in subscription")
    @RequestMapping(value = "/{subscriptionId}/unread/{writingId}", method = RequestMethod.POST)
    public Author addToUnreadList(@PathVariable(value = "authorId") Long authorId, @RequestParam(value = "writingLink") String writingLink) throws IOException {
        logger.info("Add to UnreadList, subscriptionId={}, writing.id={}",authorId, writingLink);
        return changeWritingUnreadFlag(authorId, writingLink, true);
    }

    private Author changeWritingUnreadFlag(Long authorId, String writingLink, boolean unread) throws IOException {
        Author author = authorService.findAuthor(authorId);
        Writing writing = authorService.findWritingByLink(author.getLink(), writingLink);
        writing.setUnread(false);
        return authorService.saveAuthor(author);
    }

}
