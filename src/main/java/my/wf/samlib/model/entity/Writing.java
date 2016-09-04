package my.wf.samlib.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import my.wf.samlib.model.compare.LastDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


public class Writing extends BaseEntity implements LastDate {
    private String name;
    private String link;
    private Author author;
    private String description;
    private String groupName;
    private String size;
    private String prevSize;
    private LocalDateTime lastChangedDate;
    private boolean unread;
    private Set<Changed> changesIn = new HashSet<>();

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

    @JsonBackReference
    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public LocalDateTime getLastChangedDate() {
        return lastChangedDate;
    }

    public void setLastChangedDate(LocalDateTime lastChangedDate) {
        this.lastChangedDate = lastChangedDate;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


    public boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }

    public Set<Changed> getChangesIn() {
        return changesIn;
    }

    public void setChangesIn(Set<Changed> changesIn) {
        this.changesIn = changesIn;
    }

    public String info() {
        return "Writing{" +
                "groupName='" + groupName + '\'' +
                ", name='" + getName() + '\'' +
                ", link='" + link + '\'' +
                ", author=" + (null == author? "": author.getName()) +
                ", description='" + description + '\'' +
                ", size='" + size + '\'' +
                ", prevSize='" + prevSize + '\'' +
                ", lastChangedDate=" + lastChangedDate +
                '}';
    }

    @Override
    public String toString() {
        return "Writing{" + link + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        Writing writing = (Writing) o;

        if(!getLink().equals(writing.getLink())) {
            return false;
        }
        return getAuthor() != null ? getAuthor().equals(writing.getAuthor()) : writing.getAuthor() == null;

    }

    @Override
    public int hashCode() {
        int result = null == getLink() ? super.hashCode() :  31 * getLink().hashCode();
        result = 31 * result + (getAuthor() != null ? getAuthor().hashCode() : 0);
        return result;
    }
}
