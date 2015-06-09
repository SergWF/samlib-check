package my.wf.samlib.model.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: SBilenogov
 */
public class Customer extends BaseEntity {
    private Set<Author> authors = new HashSet<Author>();
    private Set<Writing> unreadWritings = new HashSet<Writing>();
    private boolean enabled;

    public Set<Author> getAuthors(){
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Set<Writing> getUnreadWritings(){
        return unreadWritings;
    }

    public void setUnreadWritings(Set<Writing> unreadWritings) {
        this.unreadWritings = unreadWritings;
    }

    public boolean isEnabled(){
        return enabled;
    }
    public void setEnabled(boolean enabled){
        this.enabled = enabled;
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
