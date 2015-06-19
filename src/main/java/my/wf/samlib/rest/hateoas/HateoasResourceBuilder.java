package my.wf.samlib.rest.hateoas;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.rest.AuthorRestController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HateoasResourceBuilder {

    public static Resources<Resource<Author>> createResourceList(Collection<Author> authors){
        List<Resource<Author>> resources = new ArrayList<>(authors.size());
        for(Author author: authors){
            resources.add(createResource(author));
        }
        return new Resources<>(resources);
    }

    public static Resource<Author> createResource(Author author){
        Resource<Author> resource = new Resource<>(author);
        resource.add(selfLink(author));
        return resource;
    }

    static Link selfLink(Author author){
        return ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AuthorRestController.class).getDetails(author.getId())).withSelfRel();
    }
}
