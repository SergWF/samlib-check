package my.wf.samlib.web.controller;


import my.wf.samlib.model.dto.UserDto;
import my.wf.samlib.model.entity.UserData;
import my.wf.samlib.model.entity.VerificationData;
import my.wf.samlib.service.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private SecurityService securityService;

    @RequestMapping(method = RequestMethod.GET)
    public String getRegistrationForm(){
        return WebConstants.REGISTRATION_FORM;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String registration(@RequestParam("user") UserDto userDto){
        VerificationData verificationData = securityService.registerNewUser(userDto.toUserData());
        String link = securityService.generateVerificationLink(verificationData);
        logger.info("user: {}, verification link: {}", verificationData.getUserData().getUsername(), link);
        //TODO: IMPLEMENT sending email with verification link to the user
        return WebConstants.POST_REGISTRATION;
    }

    @RequestMapping(value = "/{verificationId}", method = RequestMethod.GET)
    public String verifyUser(@PathVariable("verificationId") String verificationId){
        UserData userData = securityService.verifyUser(verificationId);
        if(null == userData){
            return WebConstants.VERIFICATION_EXPIRED;
        }
        //TODO: IMPLEMENT login and forward to the main window
        return WebConstants.POST_VERIFICATION_VIEW;
    }

    //TODO: IMPLEMENT ExceptionHandling
}
