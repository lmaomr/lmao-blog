package cn.lmao.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {
    
    @GetMapping("/")
    public String redirectToApiCheck() {
        return "forward:/APICheck.html";
    }

}