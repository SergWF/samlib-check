package my.wf.samlib.updater;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Writing;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class AuthorDelta {
    private LocalDateTime timestamp;
    private Author author;
    private String authorName;
    private Set<Writing> newWritings = new HashSet<>();
    private Set<Writing> updatedWritings = new HashSet<>();
    private Set<Writing> deletedWritings = new HashSet<>();

    public AuthorDelta(Author author) {
        this.timestamp = LocalDateTime.now();
        this.author = author;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Author getAuthor() {
        return author;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Set<Writing> getNewWritings() {
        return newWritings;
    }

    public Set<Writing> getUpdatedWritings() {
        return updatedWritings;
    }

    public Set<Writing> getDeletedWritings() {
        return deletedWritings;
    }

    public boolean hasChanges(){
        return !newWritings.isEmpty() || !updatedWritings.isEmpty();
    }
}
