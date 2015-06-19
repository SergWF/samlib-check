package my.wf.samlib;

import my.wf.samlib.service.CustomerService;
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
    CustomerService customerService;

    public static void main(String[] args) {
        SpringApplication.run(SamlibWebApplication.class, args);
    }

    @PostConstruct
    public void showStatistic(){
        System.out.println(customerService.getStatistic(samlibService.getDefaultCustomer()));
    }
}
