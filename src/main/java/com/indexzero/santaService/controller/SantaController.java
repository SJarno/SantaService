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
public class SantaController {

    @Autowired
    private UserAccountService userAccountService;

    @ModelAttribute
    public UserAccount getUserAccount() {
        return new UserAccount();
    }

    @GetMapping("/santa")
    public String getSantaPage() {
        return "santa-claus";
    }

    /* Create new santa account */
    @PostMapping("/register/santa")
    public String addSantaProfile(
        @Valid @ModelAttribute UserAccount santaAccount,
        BindingResult result, 
        Model model,
        RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "santa-claus";
        }
        try {
            /* Check if username in use */
            userAccountService.createSantaAccount(santaAccount);
            
        } catch (Exception e) {
            model.addAttribute("usernameError", e.getMessage());
            return "santa-claus";
        }
        redirectAttributes.addFlashAttribute("registerSuccess", "Rekisteröinti onnistui, kirjaudu palveluun");
        return "redirect:/login-page";
    }

}
