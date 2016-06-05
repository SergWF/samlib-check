package my.wf.samlib.model.dto.builder;

import my.wf.samlib.model.dto.AuthorItemListDto;
import my.wf.samlib.model.entity.Author;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AuthorDtoBuilder {


    public static Set<AuthorItemListDto> createList(Collection<Author> authors){
        return authors.stream().map((a)-> createDto(a)).collect(Collectors.toSet());
    }

    public static AuthorItemListDto createDto(Author author) {
        AuthorItemListDto dto = new AuthorItemListDto();
        dto.setId(author.getId());
        dto.setName(author.getName());
        dto.setLink(author.getLink());
        dto.setLastChangedDate(author.getLastChangedDate());
        dto.setUnread(author.getUnread());
        return dto;
    }
}
