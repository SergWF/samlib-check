package my.wf.samlib.updater.parser.impl;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.updater.parser.AuthorChangesChecker;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
@Scope(value = "prototype",  proxyMode = ScopedProxyMode.INTERFACES)
public class AuthorChangesCheckerImpl implements AuthorChangesChecker {

    @Override
    public Set<Writing> checkUpdatedWritings(Author newAuthor, Author oldAuthor, Date checkDate){
        Set<Writing> updated = new HashSet<>();
        for(Writing writing: newAuthor.getWritings()){
            Writing oldWriting = findOldWriting(oldAuthor, writing.getLink());
            if(checkUpdatedWriting(writing, oldWriting)){
                writing.setId((null == oldAuthor)?null:oldWriting.getId());
                writing.setLastChangedDate(checkDate);
                writing.setPrevSize((null == oldAuthor)?null:oldWriting.getSize());
                updated.add(writing);
            }
        }
        return updated;
    }

    protected boolean checkUpdatedWriting(Writing newWriting, Writing oldWriting){
        return !(null == oldWriting
                && newWriting.getDescription().equals(oldWriting.getDescription())
                && newWriting.getSize().equals(oldWriting.getSize())
                && newWriting.getGroupName().equals(oldWriting.getGroupName())
                && newWriting.getSamlibDate().after(oldWriting.getSamlibDate())
        );

    }

    protected Writing findOldWriting(Author author, String writingLink){
        for(Writing writing: author.getWritings()){
            if(writing.getLink().equals(writingLink)){
                return writing;
            }
        }
        return null;
    }
}
