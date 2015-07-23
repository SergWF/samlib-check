package my.wf.samlib.rest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin", produces= MediaType.APPLICATION_JSON_VALUE)
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);


}
