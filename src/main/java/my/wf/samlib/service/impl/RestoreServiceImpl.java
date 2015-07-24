package my.wf.samlib.service.impl;

import my.wf.samlib.model.dto.backup.*;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.model.entity.Subscription;
import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.service.AuthorService;
import my.wf.samlib.service.CustomerService;
import my.wf.samlib.service.RestoreService;
import my.wf.samlib.service.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestoreServiceImpl implements RestoreService {
    private static final Logger logger = LoggerFactory.getLogger(RestoreServiceImpl.class);
    @Autowired
    AuthorService authorService;
    @Autowired
    CustomerService customerService;
    @Autowired
    SubscriptionService subscriptionService;



    @Override
    public void restore(BackupDto backupDto) {
        restoreAuthors(backupDto);
        restoreCustomers(backupDto);
    }

    protected void restoreCustomers(BackupDto backupDto) {
        logger.info("restore customers");
        long count = backupDto.getCustomers().stream().map(this::restoreSingleCustomer).count();
        logger.debug("{} customers restored", count);
    }

    protected void restoreAuthors(BackupDto backupDto) {
        logger.info("restore authors");
        long count = backupDto.getAuthors().stream().map(this::restoreSingleAuthor).count();
        logger.debug("{} authors restored", count);
    }

    protected Author restoreSingleAuthor(AuthorBackupDto authorBackupDto){
        logger.debug("restore author {} ({})", authorBackupDto.getName(), authorBackupDto.getLink());
        Author author = authorService.addAuthor(authorBackupDto.getLink());
        author = authorService.findAuthorWithWritings(author.getId());
        author.setName(authorBackupDto.getName());
        for(WritingBackupDto writingBackupDto: authorBackupDto.getWritings()){
            restoreSingleWriting(author, writingBackupDto);
        }
        return authorService.saveAuthor(author);
    }

    protected Customer restoreSingleCustomer(CustomerBackupDto customerBackupDto){
        Customer customer = new Customer();
        customer.setName(customerBackupDto.getName());
        customer = customerService.save(customer);
        for (SubscriptionBackupDto subscriptionBackupDto: customerBackupDto.getSubscriptions()){
            restoreSubscription(customer, subscriptionBackupDto);
        }
        return customerService.getCustomer(customer.getId());
    }

    private Subscription restoreSubscription(Customer customer, SubscriptionBackupDto subscriptionBackupDto) {
        Subscription subscription = subscriptionService.addAuthorAndSubscribe(customer, subscriptionBackupDto.getAuthorLink());
        for (String writingLink : subscriptionBackupDto.getUnreadWritings()) {
            Writing writing = findWriting(subscriptionBackupDto.getAuthorLink(), writingLink);
            if(null != writing){
                subscriptionService.addWritingToUnreadList(customer, subscription.getId(), writing.getId());
            }
        }
        return subscription;
    }

    private Writing findWriting(String authorLink, String writingLink) {
        return authorService.findWritingByLink(authorLink, writingLink);
    }

    private Writing createWriting(WritingBackupDto writingBackupDto){
        Writing writing = new Writing();
        writing.setLastChangedDate(writingBackupDto.getLastChangedDate());
        writingBackupDto.setLink(writingBackupDto.getLink());
        writing.setDescription(writingBackupDto.getDescription());
        writing.setName(writingBackupDto.getName());
        writing.setGroupName(writingBackupDto.getGroupName());
        writing.setPrevSize(writingBackupDto.getPrevSize());
        writing.setSize(writingBackupDto.getSize());
        return writing;
    }

    protected Writing restoreSingleWriting(Author author, WritingBackupDto writingBackupDto){
        Writing writing = createWriting(writingBackupDto);
        writing.setAuthor(author);
        author.getWritings().add(writing);
        return writing;
    }
}
