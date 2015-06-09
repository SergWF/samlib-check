package my.wf.samlib.web.controller;

import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.web.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ApplicationController {


    @Autowired
    SecurityService securityService;

    @ModelAttribute(value = "user")
    public Customer getActiveCustomer(){
        return securityService.getActiveCustomer();
    }

    @RequestMapping({"/", "/home"})
    public String getMainPage(Model model){
        return WebConstants.HOME_VIEW;
    }
}
