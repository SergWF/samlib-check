package my.wf.samlib.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import my.wf.samlib.model.compare.LastChangedDateComparator;
import my.wf.samlib.model.compare.LastDate;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "author")
public class Author extends BaseEntity implements LastDate  {

    private String name;
    private Set<Writing> writings = new HashSet<>();
    private String link;
    private Set<Subscription> subscriptions = new HashSet<>();

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name="link", unique = true)
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    public Set<Writing> getWritings() {
        return writings;
    }

    public void setWritings(Set<Writing> writings) {
        this.writings = writings;
    }


    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    public Set<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    @Transient
    @Override
    public Date getLastChangedDate() {
        if(writings.isEmpty()){
            return null;
        }
        return Collections.max(writings, new LastChangedDateComparator()).getLastChangedDate();
    }


    @Override
    public String toString() {
        return "Author{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
