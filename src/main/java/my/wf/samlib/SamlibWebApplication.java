package my.wf.samlib;

import my.wf.samlib.service.UtilsService;
import my.wf.samlib.service.SamlibService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableSwagger2
public class SamlibWebApplication {

    @Autowired
    SamlibService samlibService;
    @Autowired
    UtilsService utilsService;

    public static void main(String[] args) {
        SpringApplication.run(SamlibWebApplication.class, args);
    }

    @PostConstruct
    public void showStatistic(){
        System.out.println(utilsService.getStatistic(samlibService.getActiveCustomer()));
    }
}
