package my.wf.samlib.config;

import my.wf.samlib.model.repositoriy.UserDataRepository;
import my.wf.samlib.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class SamlibSecurityWebConfig  extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDataRepository userDataRepository;

    @Bean
    UserDetailsService userDetailsDbService(){
        UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl();
        userDetailsService.setUserDataRepository(userDataRepository);
        return userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().antMatchers("/author/**").access("hasRole('ROLE_USER')")
                .and()
                .formLogin()
                    .loginPage("/home")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .failureUrl("/home?error")
                .and()
                .logout().logoutSuccessUrl("/home")
                .and()
                .csrf();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        useDatabaseAuthentication(auth);
    }

    private void useDatabaseAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsDbService())
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    private void useInMemoryAuthentication(AuthenticationManagerBuilder auth) throws Exception {
            auth
                .inMemoryAuthentication()
                    .withUser("user").password("1").roles("USER")
                    .and()
                    .withUser("customer").password("1").roles("SOME");
    }
}
