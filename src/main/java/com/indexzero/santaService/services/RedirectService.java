package com.indexzero.santaService.services;

import java.util.Optional;

import com.indexzero.santaService.model.UserAccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.AllArgsConstructor;

@Service
public class RedirectService {

    @Autowired
    private SecurityContextService securityContextService;

    public String redirectByUserRole() {
        Optional<UserAccount> userAccount = securityContextService.getAuthenticatedUserAccount();
        if (userAccount.isPresent()) {
            String userRole = userAccount.get().getUserRole();
            return userRole.equals("ROLE_SANTA") ? "redirect:/santa-profile"
                    : userRole.equals("ROLE_CUSTOMER") ? "redirect:/customer-profile" : "redirect:/";
        }
        return "redirect:/custom-logout";
    }

    public String redirectOnSuccess(
            boolean success,
            RedirectAttributes redirectAttributes,
            String successMessage,
            String errorMessage) {
        if (success) {
            redirectAttributes.addFlashAttribute("basicInfoUpdated", successMessage);
            return redirectByUserRole();
        }
        redirectAttributes.addFlashAttribute("basicInfoNotUpdated", errorMessage);
        return redirectByUserRole();

    }

}
