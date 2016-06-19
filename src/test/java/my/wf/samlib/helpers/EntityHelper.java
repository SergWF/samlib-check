package my.wf.samlib.helpers;

import my.wf.samlib.model.dto.backup.AuthorBackupDto;
import my.wf.samlib.model.dto.backup.CustomerBackupDto;
import my.wf.samlib.model.dto.backup.SubscriptionBackupDto;
import my.wf.samlib.model.dto.backup.WritingBackupDto;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.tools.LinkTool;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

public class EntityHelper {

    public static Author createAuthor(String link, String name){
        return createAuthorWithId(null, link, name);
    }

    public static Author createAuthorWithId(Long id, String link, String name){
        Author author = new Author();
        author.setId(id);
        author.setLink(LinkTool.getAuthorLink(link, "indextitle.shtml"));
        author.setName(name);
        return author;
    }

    public static Writing createWriting(String name, Author author){
        return createWriting(name, author, LocalDateTime.now());
    }

    public static Writing createWriting(String name, Author author, LocalDateTime lastChangedDate){
        Writing writing = new Writing();
        writing.setName(name);
        writing.setLink(name + ".shtml");
        writing.setAuthor(author);
        writing.setDescription("descr");
        writing.setGroupName("grp");
        writing.setLastChangedDate(lastChangedDate);
        writing.setSize("10k");
        if(null != author) {
            author.getWritings().add(writing);
        }
        return writing;
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

    public static WritingBackupDto createWrigingBackupDto(String name, String size, Date lastChangedDate) {
        WritingBackupDto dto = new WritingBackupDto();
        dto.setLink("/" + name);
        dto.setName(name);
        dto.setSize(size);
        dto.setLastChangedDate(lastChangedDate);
        return dto;
    }


    public static AuthorBackupDto createAuthorBackupDto(String link, String name, String... writingNames) {
        AuthorBackupDto authorBackupDto = new AuthorBackupDto();
        authorBackupDto.setName(name);
        authorBackupDto.setLink(link);
        for(String writingName: writingNames){
            authorBackupDto.getWritings().add(createWritingBackupDto(writingName));
        }
        return authorBackupDto;
    }

    public static WritingBackupDto createWritingBackupDto(String writingName) {
        WritingBackupDto writingBackupDto = new WritingBackupDto();
        writingBackupDto.setLink("/" + writingName);
        writingBackupDto.setName(writingName);
        writingBackupDto.setLastChangedDate(new Date());
        writingBackupDto.setSize("10k");
        writingBackupDto.setDescription("descr for " + writingName);
        writingBackupDto.setGroupName("group " + writingName);
        writingBackupDto.setPrevSize("2k");
        return writingBackupDto;
    }

    public static CustomerBackupDto createCustomerBackupDto(String name) {
        CustomerBackupDto customerBackupDto = new CustomerBackupDto();
        customerBackupDto.setName(name);
        return customerBackupDto;
    }



    public static SubscriptionBackupDto createSubscriptionBackupDto(String  authorLink, Collection<String> writingLinks) {
        SubscriptionBackupDto subscriptionBackupDto = new SubscriptionBackupDto();
        subscriptionBackupDto.setAuthorLink(authorLink);
        subscriptionBackupDto.getUnreadWritings().addAll(writingLinks);
        return subscriptionBackupDto;
    }
}
