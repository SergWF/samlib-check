package my.wf.samlib.service.impl;

import my.wf.samlib.service.PropertyViewerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PropertyViewerServiceImpl implements PropertyViewerService {


    @Value("${samlib.check.storage.file}")
    private String storageFile;
    @Value("${cron.job.update.schedule}")
    private String cronData;
    @Value("${pause.between.authors}")
    private int pauseValue;
    @Value("${ban.check.url}")
    private String banCheckUrl;
    @Value("${skip.ban.url.checking}")
    private boolean skipBanChecking;
    @Value("${backup.path}")
    private String backupPath;

    @Value("${version.number:unknown}")
    private String versionNumber;
    @Value("${build.number:0}")
    private Integer buildNumber;
    @Value("${build.time:unknown}")
    private String buildDate;
    @Value("${server.address:http://localhost}")
    private String serverAddress;
    @Value("${server.port}")
    private String serverPort;

    public String getStorageFile() {
        return storageFile;
    }

    @Override
    public String getCronData() {
        return cronData;
    }

    @Override
    public int getPauseValue() {
        return pauseValue;
    }

    @Override
    public String getBanCheckUrl() {
        return banCheckUrl;
    }

    @Override
    public boolean getSkipBanChecking() {
        return skipBanChecking;
    }

    @Override
    public String getBackupPath() {
        return backupPath;
    }

    @Override
    public String getVersionNumber() {
        return versionNumber;
    }

    @Override
    public Integer getBuildNumber() {
        return buildNumber;
    }

    @Override
    public String getBuildDate() {
        return buildDate;
    }

    @Override
    public String getServerAddress() {
        return serverAddress;
    }

    @Override
    public String getServerPort() {
        return serverPort;
    }
}
