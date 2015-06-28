package my.wf.samlib.model.dto;

import java.util.Date;

public class WritingDto {
    private Long writingId;
    private String name;
    private String link;
    private String description;
    private String groupName;
    private String size;
    private String prevSize;
    private Date lastChangedDate;
    private boolean unread;
    private String externalUrl;

    public Long getWritingId() {
        return writingId;
    }

    public void setWritingId(Long writingId) {
        this.writingId = writingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrevSize() {
        return prevSize;
    }

    public void setPrevSize(String prevSize) {
        this.prevSize = prevSize;
    }

    public Date getLastChangedDate() {
        return lastChangedDate;
    }

    public void setLastChangedDate(Date lastChangedDate) {
        this.lastChangedDate = lastChangedDate;
    }

    public boolean getUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }
}
