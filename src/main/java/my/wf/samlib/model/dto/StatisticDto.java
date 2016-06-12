package my.wf.samlib.model.dto;

import java.time.LocalDateTime;

public class StatisticDto {
    private long totalCount;
    private long subscribedCount;
    private long notReadCount;
    private LocalDateTime lastCheckDate;

    public StatisticDto(long totalCount, long subscribedCount, long notReadCount, LocalDateTime lastCheckDate) {
        this.totalCount = totalCount;
        this.subscribedCount = subscribedCount;
        this.notReadCount = notReadCount;
        this.lastCheckDate = lastCheckDate;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public long getSubscribedCount() {
        return subscribedCount;
    }

    public long getNotReadCount() {
        return notReadCount;
    }

    public LocalDateTime getLastCheckDate() {
        return lastCheckDate;
    }
}

