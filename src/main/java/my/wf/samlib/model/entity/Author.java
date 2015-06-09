package my.wf.samlib.model.entity;

import java.util.*;


public class Author extends BaseEntity  {

    private Set<Writing> writings = new HashSet<Writing>();
    private String link;


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Set<Writing> getWritings() {
        return writings;
    }

    public void setWritings(Set<Writing> writings) {
        this.writings = writings;
    }

    public Date getLastChangedDate() {
        if(writings.isEmpty()){
            return null;
        }
        Writing writing = Collections.max(writings, new Comparator<Writing>() {
            public int compare(Writing o1, Writing o2) {
                return o1.getLastChangedDate().compareTo(o2.getLastChangedDate());
            }
        });
        return writing.getLastChangedDate();
    }

    public Boolean unreadByCustomer(Customer customer) {
        for (Writing writing : customer.getUnreadWritings()) {
            if (writing.getAuthor().equals(this)) {
                return true;
            }
        }
        return false;
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
