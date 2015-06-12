package my.wf.samlib.web.controller;


import my.wf.samlib.model.entity.Customer;
import my.wf.samlib.model.entity.UserData;
import my.wf.samlib.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

public abstract  class BaseController {
    @Autowired
    SecurityService securityService;

    @ModelAttribute(value = "user")
    public Customer getActiveCustomer(){
        UserData user = securityService.getActiveUser();
        return (null == user || !user.isEnabled())?null:user.getCustomer();
    }
}
