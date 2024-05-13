package com.example.twt.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OAuthErrorPageController {


    @GetMapping("/usernotfound")
    public String userNotFoundPage(Model model){
        model.addAttribute("Message","UnAuthorized User");
        return "error";
    }

    @GetMapping("/servererror")
    public String serverErrorPage(Model model){
        model.addAttribute("Message","OOPS! Something Went Wrong");
        return "error";
    }
}
