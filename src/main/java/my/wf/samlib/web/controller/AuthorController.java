package my.wf.samlib.web.controller;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.web.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/author")
public class AuthorController {

    private Map<Long, Author> authors = new HashMap<>();

    {
        for(int i = 1; i< 4; i++){
            createAuthor();
        }
    }

    private Author createAuthor(String url) {
        Author author = new Author();
        author.setId(newId());
        author.setName("author " + author.getId());
        author.setLink(url);
        authors.put(author.getId(), author);
        return author;
    }

    private Author createAuthor() {
        return createAuthor("http://example.com/author/"+new Date().getTime());
    }

    @Autowired
    SecurityService securityService;

    @ModelAttribute(value = "user")
    public Customer getActiveCustomer(){
        return securityService.getActiveCustomer();
    }

    @RequestMapping("/list")
    public String getAuthors(Model model){
        model.addAttribute("authors", authors.values());
        return WebConstants.AUTHORS_LIST;
    }

    @RequestMapping("/{authorId}")
    public String getDetails(@PathVariable long authorId, Model model){
        model.addAttribute("author", findAuthor(authorId));
        return WebConstants.AUTHOR_DETAILS;
    }

    private Author findAuthor(long authorId) {
        return authors.get(authorId);
    }

    @RequestMapping(method = RequestMethod.POST)

    public String addAuthor(@RequestParam("author_url") String authorUrl){
        System.out.println("ADD AUTHOR BY URL: " + authorUrl);
        createAuthor();
        return WebConstants.AUTHORS_LIST;
    }

    private long newId() {
        if(authors.isEmpty()){
            return 1;
        }
        return Collections.max(authors.keySet()) + 1;
    }

}
