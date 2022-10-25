package com.ebook.multbooks.app.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(String msg,Model model){
        model.addAttribute("msg",msg);
        return "/home/main";
    }
}
