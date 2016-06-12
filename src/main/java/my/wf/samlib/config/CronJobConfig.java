package my.wf.samlib.config;

import my.wf.samlib.storage.AuthorStorage;
import my.wf.samlib.updater.UpdateRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.time.LocalDateTime;

@Configuration
public class CronJobConfig {
    private static final Logger logger = LoggerFactory.getLogger(CronJobConfig.class);

    @Value("update.cron.enable:false")
    private boolean updateCronJobEnable;
    @Autowired
    private UpdateRunner updateRunner;
    @Autowired
    private AuthorStorage authorStorage;


    @Scheduled(cron = "${update.cron.job}")
    public void doScheduledUpdate(){
        if(updateCronJobEnable) {
            LocalDateTime date = LocalDateTime.now();
            logger.info("Run Update at {}", date);
            updateRunner.doUpdate(date);
        }
    }

    @Scheduled(cron = "${flushIfRequired.cron.job}")
    public void doFlush(){
        try {
            authorStorage.flushIfRequired();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
