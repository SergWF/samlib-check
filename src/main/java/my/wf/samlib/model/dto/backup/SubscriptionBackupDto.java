package my.wf.samlib.model.dto.backup;

import java.util.HashSet;
import java.util.Set;

public class SubscriptionBackupDto {
    private String authorLink;
    private Set<String> unreadWritings = new HashSet<>();

    public Set<String> getUnreadWritings() {
        return unreadWritings;
    }

    public void setUnreadWritings(Set<String> unreadWritings) {
        this.unreadWritings = unreadWritings;
    }

    public String getAuthorLink() {
        return authorLink;
    }

    public void setAuthorLink(String authorLink) {
        this.authorLink = authorLink;
    }
}
