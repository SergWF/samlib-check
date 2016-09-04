package my.wf.samlib.rest;

import my.wf.samlib.model.dto.UpdatingInfo;
import my.wf.samlib.service.UtilsService;
import my.wf.samlib.updater.UpdateRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/utils", produces= MediaType.APPLICATION_JSON_VALUE)
public class UtilsRestController {

    @Autowired
    UpdateRunner updateRunner;

    @Autowired
    UtilsService utilsService;


    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public void checkAuthors(){
        updateRunner.doUpdate(LocalDateTime.now());
    }


    @RequestMapping(value = "/statistic", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<UpdatingInfo> getStatistic(){
        return ResponseEntity.ok(utilsService.getUpdatingState());
    }

}
