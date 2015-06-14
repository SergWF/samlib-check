package my.wf.samlib.helpers;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.model.entity.Writing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EntityCreator {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");

    public static Author createAuthor(String link, String name){
        Author author = new Author();
        author.setLink(link);
        author.setName(name);
        return author;
    }

    public static Writing createWriting(String name, Author author){
        Writing writing = new Writing();
        writing.setName(name);
        writing.setLink("http://" + name);
        writing.setAuthor(author);
        writing.setDescription("descr");
        writing.setGroupName("grp");
        writing.setLastChangedDate(new Date());
        writing.setSize("10k");
        return writing;
    }

    public static Customer createDefaultCustomer(){
        Customer defaultCustomer = new Customer();
        defaultCustomer.setName("default");
        return defaultCustomer;
    }

    public static Customer createCustomerForRss() throws ParseException {
        Author a1 = EntityCreator.createAuthor("http://link1", "a1");
        a1.setId(1L);
        Writing w11 = EntityCreator.createWriting("w11", a1);
        w11.setId(11L);
        w11.setLastChangedDate(sdf.parse("2015.02.11 10:00:00"));
        Writing w12 = EntityCreator.createWriting("w12", a1);
        w12.setId(12L);
        w12.setLastChangedDate(sdf.parse("2015.02.11 11:00:00"));

        Author a2 = EntityCreator.createAuthor("http://link1", "a1");
        a2.setId(2L);
        Writing w21 = EntityCreator.createWriting("w21", a2);
        w21.setId(21L);
        w21.setLastChangedDate(sdf.parse("2015.02.11 21:00:00"));
        Writing w22 = EntityCreator.createWriting("w22", a2);
        w21.setId(22L);
        w22.setLastChangedDate(sdf.parse("2015.02.11 22:00:00"));

        Customer customer = EntityCreator.createDefaultCustomer();
        customer.getAuthors().add(a1);
        customer.getAuthors().add(a2);
        return customer;
    }


}
