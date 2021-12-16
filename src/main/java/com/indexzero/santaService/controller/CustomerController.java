package com.indexzero.santaService.controller;

import javax.validation.Valid;

import com.indexzero.santaService.model.UserAccount;
import com.indexzero.santaService.services.UserAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class CustomerController {

    @Autowired
    private UserAccountService userAccountService;

    @ModelAttribute
    private UserAccount getCustomer() {
        return new UserAccount();
    }

    @GetMapping("/customer")
    public String getCustomerPage() {
        return "customer";
    }
    @PostMapping("/register/customer")
    public String register(
        @Valid @ModelAttribute UserAccount customerAccount, 
        BindingResult result, 
        Model model,
        RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "customer";
        }
        try {
            userAccountService.createUserAccount(customerAccount, "customer");
        } catch (Exception e) {
            model.addAttribute("usernameError", e.getMessage());
            return "customer";
        }
        redirectAttributes.addFlashAttribute("registerSuccess", "Rekisteröinti onnistui, kirjaudu palveluun");
        return "redirect:/login-page";
    }

}
