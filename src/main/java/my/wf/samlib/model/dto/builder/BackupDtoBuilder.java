package my.wf.samlib.model.dto.builder;

import my.wf.samlib.model.dto.backup.AuthorBackupDto;
import my.wf.samlib.model.dto.backup.CustomerBackupDto;
import my.wf.samlib.model.dto.backup.SubscriptionBackupDto;
import my.wf.samlib.model.dto.backup.WritingBackupDto;
import my.wf.samlib.model.entity.*;

public class BackupDtoBuilder {

    public static AuthorBackupDto createAuthorBackupDto(Author author){
        AuthorBackupDto dto = new AuthorBackupDto();
        dto.setName(author.getName());
        dto.setLink(author.getLink());
        for(Writing writing: author.getWritings()){
            dto.getWritings().add(createWritingBackupDto(writing));
        }
        return dto;
    }

    protected static WritingBackupDto createWritingBackupDto(Writing writing) {
        WritingBackupDto dto = new WritingBackupDto();
        dto.setLink(writing.getLink());
        dto.setName(writing.getName());
        dto.setDescription(writing.getDescription());
        dto.setGroupName(writing.getGroupName());
        dto.setLastChangedDate(writing.getLastChangedDate());
        dto.setPrevSize(writing.getPrevSize());
        dto.setSize(writing.getSize());
        return dto;
    }

    public static CustomerBackupDto createCustomerBackupDto(Customer customer){
        CustomerBackupDto dto = new CustomerBackupDto();
        dto.setName(customer.getName());
        for(Subscription subscription: customer.getSubscriptions()){
            dto.getSubscriptions().add(createSubscriptionBackupDto(subscription));
        }
        return dto;
    }

    protected static SubscriptionBackupDto createSubscriptionBackupDto(Subscription subscription) {
        SubscriptionBackupDto dto = new SubscriptionBackupDto();
        dto.setAuthorLink(subscription.getAuthor().getLink());
        for(SubscriptionUnread subscriptionUnread: subscription.getSubscriptionUnreads()){
            dto.getUnreadWritings().add(subscriptionUnread.getWriting().getLink());
        }
        return dto;
    }
}
