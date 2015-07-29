package my.wf.samlib.service;

public interface PropertyViewerService {
    String getStorageUrl();
    String getCronData();
    int getPauseValue();
    String getBanCheckUrl();
    boolean getSkipBanChecking();
    String getBackupPath();

    String getVersionNumber();
    Integer getBuildNumber();
    String getBuildDate();

}
