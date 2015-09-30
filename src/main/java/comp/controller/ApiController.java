package comp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


/**
 * Created by liushanchen on 15/9/28.
 */
@Controller
public class ApiController {

    @RequestMapping("/home")
    public ModelAndView home_page() {
        ModelAndView mav = new ModelAndView("/index.html");
        return mav;
    }


    @RequestMapping("/test")
    public String mytest(){

        return "redirect:/index.html";
    }

}
