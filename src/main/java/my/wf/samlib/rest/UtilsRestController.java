package my.wf.samlib.rest;

import my.wf.samlib.model.dto.StatisticDto;
import my.wf.samlib.service.UtilsService;
import my.wf.samlib.updater.UpdateRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/utils", produces= MediaType.APPLICATION_JSON_VALUE)
public class UtilsRestController {

    @Autowired
    UpdateRunner updateRunner;

    @Autowired
    UtilsService utilsService;


    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public void checkAuthors(){
        updateRunner.runUpdate();
    }


    @RequestMapping(value = "/statistic", method = RequestMethod.GET)
    @ResponseBody
    public StatisticDto getStatistic(){
        return utilsService.getStatistic();
    }

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    @ResponseBody
    public Integer doImport(@RequestBody List<String> authorLinks) throws IOException {
        return utilsService.importAuthors(authorLinks);
    }

    @RequestMapping(value = "/export", method = RequestMethod.GET)
    @ResponseBody
    public Set<String> doExport(){
        return utilsService.exportAuthors();
    }
}
