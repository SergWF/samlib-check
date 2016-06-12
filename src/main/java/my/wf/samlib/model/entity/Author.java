package my.wf.samlib.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import my.wf.samlib.model.compare.LastDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


public class Author extends BaseEntity implements LastDate  {

    private String name;
    private Set<Writing> writings = new HashSet<>();
    private String link;

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

    @JsonManagedReference
    public Set<Writing> getWritings() {
        return writings;
    }

    public void setWritings(Set<Writing> writings) {
        this.writings = writings;
    }


    @Override
    public LocalDateTime getLastChangedDate() {
        if(writings.isEmpty()){
            return null;
        }
        return writings.stream().max((w1, w2)->w1.getLastChangedDate().compareTo(w2.getLastChangedDate())).get().getLastChangedDate();
        //return Collections.max(writings, new LastChangedDateComparator()).getLastChangedDate();
    }


    @Override
    public String toString() {
        return "Author{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", link='" + link + '\'' +
                '}';
    }

    @JsonIgnore
    public long getUnread() {
        return writings.stream().filter((w)->w.isUnread()).count();
    }
}
