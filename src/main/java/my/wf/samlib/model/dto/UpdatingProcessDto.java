package my.wf.samlib.model.dto;

import my.wf.samlib.model.entity.Author;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UpdatingProcessDto {
    private int total;
    private int processed;
    private Map<Author, RuntimeException> errors = new HashMap<>();
    private LocalDateTime date;
    private Map<Author, Integer> authorsUpdated = new HashMap<>();


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getProcessed() {
        return processed;
    }

    public void setProcessed(int processed) {
        this.processed = processed;
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

    public Map<Author, Integer> getAuthorsUpdated() {
        return authorsUpdated;
    }

    public void increaseProcessed(int delta){
        processed += delta;
    }

}
