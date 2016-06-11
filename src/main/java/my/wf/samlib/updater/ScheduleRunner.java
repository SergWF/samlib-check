package my.wf.samlib.updater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Date;

@Component
@ConditionalOnProperty(name = "update.cron.enable")
public class ScheduleRunner {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleRunner.class);

    @Autowired
    UpdateRunner updateRunner;


    @PostConstruct
    public void postConstruct(){
    }


    @Scheduled(cron = "${update.cron.job}")
    public void doScheduledUpdate(){
        LocalDateTime date = LocalDateTime.now();
        logger.info("Run Update at {}", date);
        updateRunner.doUpdate(date);
    }
}
