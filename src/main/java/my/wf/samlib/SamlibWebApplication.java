package my.wf.samlib;

import my.wf.samlib.service.PropertyViewerService;
import my.wf.samlib.service.UtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.net.UnknownHostException;

@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration
@PropertySources({
        @PropertySource("classpath:default.properties"),
        @PropertySource(value = "file:${external.config}", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:version.properties", ignoreResourceNotFound = true)
})
public class SamlibWebApplication {

    @Autowired
    UtilsService utilsService;


    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SamlibWebApplication.class, args);
        printProperties(context.getBean(PropertyViewerService.class));
    }

    public static void printProperties(PropertyViewerService propertyViewerService) {
        System.out.println("storage file    : " + new File(propertyViewerService.getStorageFile()).getAbsolutePath());
        System.out.println("backup path    : " + propertyViewerService.getBackupPath());
        System.out.println("cron data      : " + propertyViewerService.getCronData());
        System.out.println("Update pause   : " + propertyViewerService.getPauseValue());
        System.out.println("banCheck Url   : " + propertyViewerService.getBanCheckUrl());
        System.out.println("Skip Ban Check : " + propertyViewerService.getSkipBanChecking());

        System.out.println("version : " + propertyViewerService.getVersionNumber());
        System.out.println("build : " + propertyViewerService.getBuildNumber());
        System.out.println("build date: " + propertyViewerService.getBuildDate());
        System.out.println("Application URL: " + propertyViewerService.getServerAddress() + ":" + propertyViewerService.getServerPort());
    }


}
