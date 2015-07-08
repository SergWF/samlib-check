package my.wf.samlib.service;

public interface BackupService {

    String backup();

    void restore(String backupLabel);

    void restore();
}
