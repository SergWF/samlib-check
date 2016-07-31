package my.wf.samlib.helpers;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Changed;
import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.storage.AuthorStorage;

import java.util.Arrays;
import java.util.Set;

public class InitHelper {

    public static void initAuthors(AuthorStorage authorStorage){
        Author author1 = EntityHelper.createAuthorWithId(1L, "http://a1", "a1");
        Author author2 = EntityHelper.createAuthorWithId(2L, "http://a2", "a2");
        Author author3 = EntityHelper.createAuthorWithId(3L, "http://a3", "a3");
        Writing w1 = EntityHelper.createWriting("w1", author1);
        Writing w2 = EntityHelper.createWriting("w2", author1);
        w2.setUnread(true);
        w2.getChangesIn().add(Changed.NEW);
        Writing w3 = EntityHelper.createWriting("w3", author2);
        Writing w4 = EntityHelper.createWriting("w4", author2);
        Writing w5 = EntityHelper.createWriting("w5", author3);
        w5.setUnread(true);
        w5.getChangesIn().addAll(Arrays.asList(Changed.NAME, Changed.SIZE));
        Writing w6 = EntityHelper.createWriting("w6", author3);
        w6.setUnread(true);
        w6.getChangesIn().addAll(Arrays.asList(Changed.NAME, Changed.SIZE, Changed.DESCRIPTION));
        Writing w7 = EntityHelper.createWriting("w7", author3);
        Set<Author> all = authorStorage.getAll();
        for(Author author: all){
            authorStorage.delete(author);
        }
        authorStorage.save(author1);
        authorStorage.save(author2);
        authorStorage.save(author3);
    }
}
