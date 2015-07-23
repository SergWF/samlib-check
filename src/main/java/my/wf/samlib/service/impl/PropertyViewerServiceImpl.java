package my.wf.samlib.service.impl;

import my.wf.samlib.service.PropertyViewerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PropertyViewerServiceImpl implements PropertyViewerService {


    @Value("${spring.datasource.url}")
    private String storageUrl;
    @Value("${update.cron.job}")
    private String cronData;
    @Value("${pause.between.authors}")
    private int pauseValue;
    @Value("${ban.check.url}")
    private String banCheckUrl;
    @Value("${skip.ban.url.checking}")
    private boolean skipBanChecking;
    @Value("${backup.path}")
    private String backupPath;

    @Override
    public String getStorageUrl() {
        return storageUrl;
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
}
