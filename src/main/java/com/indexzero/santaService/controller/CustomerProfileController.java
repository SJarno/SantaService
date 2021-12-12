package com.indexzero.santaService.controller;

import java.util.Optional;

import com.indexzero.santaService.model.CustomerProfile;
import com.indexzero.santaService.model.UserAccount;
import com.indexzero.santaService.repositories.CustomerProfileRepository;
import com.indexzero.santaService.services.CustomerProfileService;
import com.indexzero.santaService.services.UserAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerProfileController {

    @Autowired
    private UserAccountService userAccountService;

    // @Secured("ROLE_CUSTOMER")
    @GetMapping("/customer-profile")
    public String customerProfileView(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated()) {

            /* Get useraccount */
            Optional<UserAccount> userAccount = userAccountService.findUserAccountByUsername(auth.getName());
            if (userAccount.isPresent()) {
                model.addAttribute("basicInfo", userAccount.get());
                model.addAttribute("profileInfo", userAccount.get().getCustomerProfile());
                return "customer-profile";
            }
        }

        return "redirect:login-page";
    }

}
