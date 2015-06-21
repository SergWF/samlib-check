package my.wf.samlib.updater;

import my.wf.samlib.model.dto.UpdatingProcessDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateRunner {

    @Autowired
    AuthorChecker authorChecker;


    public synchronized UpdatingProcessDto runUpdate(){
        UpdatingProcessDto processDto = authorChecker.getUpdateStatus();
        System.out.println(processDto);
        if(processDto.inProcess()){
            System.out.println("IN PROCESS!");
            return processDto;
        }
        System.out.println("RUN UPDATE!");
        return authorChecker.doCheckUpdates();
    }

    public UpdatingProcessDto getStatistic(){
        return authorChecker.getUpdateStatus();
    }
}
