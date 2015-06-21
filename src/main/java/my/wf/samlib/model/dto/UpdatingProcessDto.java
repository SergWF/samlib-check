package my.wf.samlib.model.dto;

import java.util.Date;

public class UpdatingProcessDto {
    private int total;
    private int processed;
    private Date date;

    public UpdatingProcessDto(int total, int processed) {
        this.total = total;
        this.processed = processed;
        this.date = new Date();
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

    @Override
    public String toString() {
        return "UpdatingProcessDto{" +
                "total=" + total +
                ", processed=" + processed +
                ", date=" + date +
                ", inProcess=" + inProcess() +
                '}';
    }
}
