package my.wf.samlib.model.dto;

import java.util.Date;

public class UpdatingProcessDto {
    private int total;
    private int processed;
    private int errors;
    private Date date;

    public UpdatingProcessDto(int total, int processed, int errors) {
        this.total = total;
        this.processed = processed;
        this.date = new Date();
        this.errors = errors;
    }

    public int getTotal() {
        return total;
    }

    public int getProcessed() {
        return processed;
    }

    public Date getDate() {
        return date;
    }

    public boolean inProcess(){
        return processed < total;
    }

    public int getErrors() {
        return errors;
    }

    @Override
    public String toString() {
        return "UpdatingProcessDto{" +
                "total=" + total +
                ", processed=" + processed +
                ", errors=" + errors +
                ", date=" + date +
                ", inProcess=" + inProcess() +
                '}';
    }
}
