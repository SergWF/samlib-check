package my.wf.samlib.rest;

import com.wordnik.swagger.annotations.ApiOperation;
import my.wf.samlib.model.dto.StatisticDto;
import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.service.SamlibService;
import my.wf.samlib.service.UtilsService;
import my.wf.samlib.updater.UpdateRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/utils", produces= MediaType.APPLICATION_JSON_VALUE)
public class UtilsRestController {

    @Autowired
    UpdateRunner updateRunner;
    @Autowired
    SamlibService samlibService;
    @Autowired
    UtilsService utilsService;


    @ApiOperation(value = "Triggers check of author updates")
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public void checkAuthors(){
        updateRunner.runUpdate();
    }


    @ApiOperation(value = "Returns statistic of selected customer")
    @RequestMapping(value = "/statistic", method = RequestMethod.GET)
    @ResponseBody
    public StatisticDto getStatistic(){
        return utilsService.getStatistic(getActiveCustomer());
    }

    @ApiOperation(value = "Uses for adding authors in bulk mode")
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    @ResponseBody
    public Integer doImport(@RequestBody List<String> authorLinks){
        return utilsService.importAuthors(getActiveCustomer(), authorLinks);
    }

    @ApiOperation(value = "Returns list of Author's links")
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    @ResponseBody
    public Set<String> doExport(){
        return utilsService.exportAuthors();
    }

    private Customer getActiveCustomer() {
        return samlibService.getActiveCustomer();
    }
}
