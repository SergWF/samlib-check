package my.wf.samlib.helpers;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.model.entity.Writing;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

public class EntityHelper {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");

    public static Author createAuthor(String link, String name){
        Author author = new Author();
        author.setLink(link);
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

    public static Customer createCustomer(String name, Author... authors){
        Customer customer = new Customer();
        customer.setName(name);
        if(authors.length > 0) {
            customer.getAuthors().addAll(Arrays.asList(authors));
        }
        return customer;
    }

    public static Customer createDefaultCustomer(){
        return createCustomer("default");
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
