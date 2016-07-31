package my.wf.samlib.model.dto;

import my.wf.samlib.model.entity.Author;
import my.wf.samlib.updater.AuthorDelta;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UpdatingInfo {
    private int total;
    private Map<Author, RuntimeException> errors = new HashMap<>();
    private LocalDateTime date;
    private Set<AuthorDelta> authorsUpdates = new HashSet<>();


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getProcessed() {
        return getAuthorsUpdates().size();
    }

    public Map<Author, RuntimeException> getErrors() {
        return errors;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Set<AuthorDelta> getAuthorsUpdates() {
        return authorsUpdates;
    }

}
