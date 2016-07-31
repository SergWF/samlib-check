package my.wf.samlib.updater;

import my.wf.samlib.model.dto.UpdatingInfo;

import java.time.LocalDateTime;

public interface UpdateRunner {

    void doUpdate(LocalDateTime checkDate);

    UpdatingInfo getState();
}
