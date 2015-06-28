package my.wf.samlib.model.dto.builder;

import my.wf.samlib.model.dto.AuthorDetailsDto;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Subscription;

public class AuthorDtoBuilder {

    public static AuthorDetailsDto buildDto(Author author, Subscription subscription){
        AuthorDetailsDto dto = new AuthorDetailsDto();
        dto.setSubscriptionId(subscription.getId());
        dto.setAuthorId(author.getId());
        dto.setName(author.getName());
        dto.setLink(author.getLink());
        dto.setUnread(subscription.getSubscriptionUnreads().size());
        dto.getWritings().addAll(WritingDtoBuilder.buildDtoSet(author.getWritings(), subscription));
        return dto;
    }
}
