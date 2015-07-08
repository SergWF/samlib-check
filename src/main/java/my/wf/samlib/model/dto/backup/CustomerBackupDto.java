package my.wf.samlib.model.dto.backup;

import java.util.HashSet;
import java.util.Set;

public class CustomerBackupDto {
    private String name;
    private Set<SubscriptionBackupDto> subscriptions = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<SubscriptionBackupDto> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<SubscriptionBackupDto> subscriptions) {
        this.subscriptions = subscriptions;
    }
}
