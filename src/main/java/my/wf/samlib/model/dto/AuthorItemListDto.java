package my.wf.samlib.model.dto;

import java.time.LocalDateTime;
import java.util.Date;

public class AuthorItemListDto {

    private Long id;
    private String name;
    private String link;
    private LocalDateTime lastChangedDate;
    private long unread;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }


    public void setLastChangedDate(LocalDateTime lastChangedDate) {
        this.lastChangedDate = lastChangedDate;
    }

    public LocalDateTime getLastChangedDate() {
        return lastChangedDate;
    }

    public void setUnread(long unread) {
        this.unread = unread;
    }

    public long getUnread() {
        return unread;
    }
}
