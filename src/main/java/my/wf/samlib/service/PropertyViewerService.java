package my.wf.samlib.service;

import java.net.UnknownHostException;

public interface PropertyViewerService {
    String getStorageFile();
    String getCronData();
    int getPauseValue();
    String getBanCheckUrl();
    boolean getSkipBanChecking();
    String getBackupPath();

    String getVersionNumber();
    Integer getBuildNumber();
    String getBuildDate();

    String getServerAddress();

    String getServerPort();
}
