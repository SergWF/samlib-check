package my.wf.samlib.helpers;

import my.wf.samlib.model.entity.*;
import my.wf.samlib.tools.LinkTool;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

public class EntityHelper {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");

    public static Author createAuthor(String link, String name){
        Author author = new Author();
        author.setLink(LinkTool.getAuthorLink(link, "indextitle.shtml"));
        author.setName(name);
        return author;
    }

    public static Writing createWriting(String name, Author author){
        return createWriting(name, author, new Date());
    }

    public static Writing createWriting(String name, Author author, Date lastChangedDate){
        Writing writing = new Writing();
        writing.setName(name);
        writing.setLink("http://" + name);
        writing.setAuthor(author);
        writing.setDescription("descr");
        writing.setGroupName("grp");
        writing.setLastChangedDate(lastChangedDate);
        writing.setSize("10k");
        author.getWritings().add(writing);
        return writing;
    }

    public static Subscription createSubscription(Customer customer, Author author){
        Subscription subscription = new Subscription();
        subscription.setAuthor(author);
        subscription.setCustomer(customer);
        customer.getSubscriptions().add(subscription);
        subscription.setSubscribedDate(new Date());
        for(Writing writing: author.getWritings()){
            subscription.getSubscriptionUnreads().add(createSubscriptionUnread(subscription, writing));
        }
        return subscription;
    }

    private static SubscriptionUnread createSubscriptionUnread(Subscription subscription, Writing writing) {
        SubscriptionUnread subscriptionUnread = new SubscriptionUnread();
        subscriptionUnread.setSubscription(subscription);
        subscriptionUnread.setWriting(writing);
        subscription.getSubscriptionUnreads().add(subscriptionUnread);
        return subscriptionUnread;
    }

    public static Customer createCustomer(String name, Author... authors){
        Customer customer = new Customer();
        customer.setName(name);
        for(Author author: authors){
            customer.getSubscriptions().add(createSubscription(customer, author));
        }
        return customer;
    }


    public static Author makeCopy(Author authorTemplate){
        Author author = new Author();
        author.setLink(authorTemplate.getLink());
        author.setName(authorTemplate.getName());
        for(Writing writingTemplate: authorTemplate.getWritings()){
            author.getWritings().add(makeCopy(writingTemplate));
        }
        return author;
    }

    public static Writing makeCopy(Writing writingTemplate){
        Writing writing = new Writing();
        writing.setName(writingTemplate.getName());
        writing.setLink(writingTemplate.getLink());
        writing.setDescription(writingTemplate.getDescription());
        writing.setGroupName(writingTemplate.getGroupName());
        writing.setLastChangedDate(writingTemplate.getLastChangedDate());
        writing.setSize(writingTemplate.getSize());
        return writing;
    }

    public static Writing findByLink(String link, Collection<Writing> writings){
        for(Writing writing: writings){
            if(writing.getLink().equals(link)){
                return writing;
            }
        }
        return null;
    }
    public static Writing findByName(String name, Collection<Writing> writings){
        for(Writing writing: writings){
            if(writing.getName().equals(name)){
                return writing;
            }
        }
        return null;
    }

    public static String loadPage(String path) throws IOException {
        return IOUtils.toString(EntityHelper.class.getResourceAsStream(path), Charset.forName("Cp1251"));
    }
}
