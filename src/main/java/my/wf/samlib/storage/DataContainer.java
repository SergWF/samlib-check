package my.wf.samlib.storage;

import my.wf.samlib.model.entity.Author;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DataContainer {
    private LocalDateTime lastUpdateTime;
    private Set<Author> authors = new HashSet<>();

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Collection<Author> authors) {
        this.authors = new HashSet<>(authors);
    }
}
