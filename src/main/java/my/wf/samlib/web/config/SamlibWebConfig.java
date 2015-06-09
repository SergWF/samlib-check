package my.wf.samlib.web.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableAutoConfiguration
@EnableWebMvc
public class SamlibWebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/home").setViewName(WebConstants.HOME_VIEW);
//        registry.addViewController("/").setViewName(WebConstants.HOME_VIEW);
//        registry.addViewController("/hello").setViewName(WebConstants.HELLO_VIEW);
//        registry.addViewController("/login").setViewName(WebConstants.LOGIN_VIEW);
    }

}
