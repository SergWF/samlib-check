package my.wf.samlib.model.dto;

import java.util.HashSet;
import java.util.Set;

public class AuthorDetailsDto {
    private Long subscriptionId;
    private Long authorId;
    private String name;
    private String Link;
    private int unread;
    private Set<WritingDto> writings = new HashSet<>();

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public Set<WritingDto> getWritings() {
        return writings;
    }

    public void setWritings(Set<WritingDto> writings) {
        this.writings = writings;
    }
}
