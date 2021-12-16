package com.indexzero.santaService.controller;

import com.indexzero.santaService.model.UserAccount;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class DefaultController {

    @ModelAttribute
    public UserAccount getUserAccount() {
        return new UserAccount();
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

}
