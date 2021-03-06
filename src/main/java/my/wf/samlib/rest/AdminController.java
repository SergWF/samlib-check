package my.wf.samlib.rest;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import my.wf.samlib.model.dto.VersionInfoDto;
import my.wf.samlib.model.dto.backup.BackupDto;
import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/admin", produces= MediaType.APPLICATION_JSON_VALUE)
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

    @Autowired
    UtilsService utilsService;
    @Autowired
    SamlibService samlibService;
    @Autowired
    BackupService backupService;
    @Autowired
    RestoreService restoreService;
    @Autowired
    PropertyViewerService propertyViewerService;



    @ApiOperation(value = "Import author list from customer")
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    @ResponseBody
    public Integer doImport(@RequestBody MultipartFile file) throws IOException {
        if(file.isEmpty()){
            return -1;
        }
        String jsonData = new String(file.getBytes(), StandardCharsets.UTF_8);
        logger.debug("import Authors");
        logger.debug(jsonData);
        return utilsService.importAuthors(getActiveCustomer(), new ObjectMapper().readValue(jsonData, new TypeReference<List<String>>() {
        }));
    }

    @ApiOperation(value = "Export customer author lkist")
    @RequestMapping(value = "/export", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> doExport() throws JsonProcessingException {
        return makeFileForDownload(utilsService.exportAuthors(), "export-" + SIMPLE_DATE_FORMAT.format(new Date()) + ".json");
    }

    @ApiOperation(value = "Backups all data to a file")
    @RequestMapping(value = "/backup", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]>  doBackup() throws IOException {
        return makeFileForDownload(backupService.backup(), "backup-"+SIMPLE_DATE_FORMAT.format(new Date())+".json");
    }

    @ApiOperation(value = "Restores all data from sends a file")
    @RequestMapping(value = "/restore", method = RequestMethod.POST)
    public Integer doRestore(@RequestBody MultipartFile file) throws IOException {
        if(file.isEmpty()){
            return -1;
        }
        String jsonData = new String(file.getBytes(), StandardCharsets.UTF_8);
        logger.debug("restore data");
        logger.debug(jsonData);
        restoreService.restore(new ObjectMapper().readValue(jsonData, BackupDto.class));
        return 1;
    }

    @ApiOperation(value = "Version Info")
    @RequestMapping(value = "/version", method = RequestMethod.GET)
    @ResponseBody
    public VersionInfoDto getVersionInfo(){
        return new VersionInfoDto(propertyViewerService.getVersionNumber(), propertyViewerService.getBuildNumber(), propertyViewerService.getBuildDate());
    }


    private ResponseEntity<byte[]> makeFileForDownload(Object data, String fileName) throws JsonProcessingException {
        String json = new ObjectMapper().writeValueAsString(data);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-disposition", "attachment; filename=" + fileName);
        return new ResponseEntity(json.getBytes(), responseHeaders, HttpStatus.OK);
    }


    private Customer getActiveCustomer() {
        return samlibService.getActiveCustomer();
    }
}
