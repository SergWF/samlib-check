package my.wf.samlib.updater;

import my.wf.samlib.model.dto.UpdatingProcessDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateRunner {

    private static final Logger logger = LoggerFactory.getLogger(UpdateRunner.class);
    @Autowired
    private AuthorChecker authorChecker;
    private UpdatingProcessDto updatingState;


    public synchronized UpdatingProcessDto runUpdate(){
        UpdatingProcessDto processDto = authorChecker.getUpdateStatus();
        logger.debug(processDto.toString());
        if(processDto.inProcess()){
            logger.debug("IN PROCESS!");
            return processDto;
        }
        logger.info("RUN UPDATE!");
        return authorChecker.doCheckUpdates();
    }


    public UpdatingProcessDto getStatistic(){
        return authorChecker.getUpdateStatus();
    }
}
