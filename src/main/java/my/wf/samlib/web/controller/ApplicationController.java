package my.wf.samlib.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ApplicationController extends BaseController {

    @RequestMapping({"/", "/home"})
    public String getMainPage(Model model){
        return WebConstants.HOME_VIEW;
    }
}
