package my.wf.samlib.service;

import my.wf.samlib.model.dto.backup.BackupDto;

public interface RestoreService {

    void restore(BackupDto backupDto);
}
