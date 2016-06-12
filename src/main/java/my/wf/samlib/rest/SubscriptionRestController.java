package my.wf.samlib.rest;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping(value = "/authors", produces= MediaType.APPLICATION_JSON_VALUE)
public class SubscriptionRestController {

    private static final Logger logger  = LoggerFactory.getLogger(SubscriptionRestController.class);

    @Autowired
    AuthorService authorService;



    @RequestMapping(value = "/subscribe/url", method = RequestMethod.POST)
    public Author subscribe(@RequestBody String authorUrl) throws IOException {
        return authorService.addAuthor(authorUrl);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Set<AuthorItemListDto> getAuthorList(){
        return AuthorDtoBuilder.createList(authorService.findAllAuthors());
    }

    @RequestMapping(value = "/{authorId}", method = RequestMethod.GET)
    public Author getSubscriptionById(@PathVariable(value = "authorId") long authorId){
        return authorService.findAuthor(authorId);
    }

    @RequestMapping(value = "/{authorId}/unread/all", method = RequestMethod.DELETE)
    public AuthorItemListDto markAllAsRead(@PathVariable(value = "authorId") Long authorId) throws IOException {
        Author author = authorService.findAuthor(authorId);
        authorService.markAllWritingsRead(author);
        return AuthorDtoBuilder.createDto(authorService.findAuthor(authorId));
    }

    @RequestMapping(value = "/{authorId}/unread/", method = RequestMethod.DELETE)
    public Author removeFromUnreadList(@PathVariable(value = "authorId") Long authorId, @RequestParam(value = "writingLink") String writingLink) throws IOException {
        logger.info("Remove from UnreadList, authorId={}, writing.link={}",authorId, writingLink);
        return changeWritingUnreadFlag(authorId, writingLink, false);
    }

    @RequestMapping(value = "/{authorId}/unread/{writingId}", method = RequestMethod.POST)
    public Author addToUnreadList(@PathVariable(value = "authorId") Long authorId, @RequestParam(value = "writingLink") String writingLink) throws IOException {
        logger.info("Add to UnreadList, subscriptionId={}, writing.id={}",authorId, writingLink);
        return changeWritingUnreadFlag(authorId, writingLink, true);
    }

    @RequestMapping(value = "/{authorId}", method = RequestMethod.DELETE)
    @ResponseBody
    public Long deleteAuthor(@PathVariable long authorId) throws IOException {
        logger.info("DELETE AUTHOR BY ID {}", authorId);
        authorService.delete(authorId);
        return authorId;
    }

    private Author changeWritingUnreadFlag(Long authorId, String writingLink, boolean unread) throws IOException {
        Author author = authorService.findAuthor(authorId);
        Writing writing = authorService.findWritingByLink(author.getLink(), writingLink);
        writing.setUnread(false);
        return authorService.saveAuthor(author);
    }

}
