package my.wf.samlib.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="customer")
@AttributeOverrides({
        @AttributeOverride(name="name", column=@Column(name="name", unique = true))
})
public class Customer extends BaseEntity {
    private Set<Author> authors = new HashSet<Author>();
    private Set<Writing> unreadWritings = new HashSet<Writing>();


    @ManyToMany
    @JoinTable(name = "customer_author"
            , joinColumns = @JoinColumn(name = "customer_id")
            , inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    @JsonManagedReference
    public Set<Author> getAuthors(){
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    @ManyToMany
    @JoinTable(name = "customer_unread_writing"
            , joinColumns = @JoinColumn(name = "customer_id")
            , inverseJoinColumns = @JoinColumn(name = "writing_id")
    )
    @JsonManagedReference
    public Set<Writing> getUnreadWritings(){
        return unreadWritings;
    }

    public void setUnreadWritings(Set<Writing> unreadWritings) {
        this.unreadWritings = unreadWritings;
    }


    public boolean checkAuthorUnread(Author author){
        for(Writing w: author.getWritings()){
            if(getUnreadWritings().contains(w)){
                return true;
            }
        }
        return false;
    }

    public boolean checkWritingUnread(Writing writing){
        return getUnreadWritings().contains(writing);
    }

}
