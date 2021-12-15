package com.indexzero.santaService.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.indexzero.santaService.model.UserAccount;
import com.indexzero.santaService.services.UserAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserAccountController {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authManager;

    /* Create */

    /* Read */

    /* Include only what needed, santa roles */
    @ResponseBody
    @RequestMapping(value = "santa-users", method = RequestMethod.GET, produces = "application/json")
    public List<UserAccount> getNewSantas() {
        return userAccountService.getNewSantas();
    }

    /* Update basic account info: */
    @PostMapping("/update/account-basic")
    public String updateUserAccount(
            // @RequestParam String username,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String phonenumber,
            @RequestParam String postalCode,
            @RequestParam(required = true) String password,
            RedirectAttributes redirectAttributes) {

        Optional<UserAccount> oldAccount = userAccountService
                .findUserAccountByUsername(getAuthenticatedUser().getName());
        boolean success = false;
        if (oldAccount.isPresent() && checkIfAuthenticated(password, oldAccount.get().getPassword())) {
            UserAccount newAccount = new UserAccount();
            newAccount.setFirstName(firstName);
            newAccount.setLastName(lastName);
            newAccount.setEmail(email);
            newAccount.setPhoneNumber(phonenumber);
            newAccount.setPostalCode(postalCode);
            success = userAccountService.updateAccountInfo(oldAccount, newAccount);
            /* if successful add message and redirect by role */
            return redirectOnSuccess(
                    success, redirectAttributes, "Perustiedot päivitetty",
                    "Jotain meni vikaan, perustietoja ei päivietty");

        }
        redirectAttributes.addFlashAttribute("basicInfoNotUpdated", "Väärä salasana!");
        return redirectByUserRole();

    }

    /* Update username */
    @PostMapping("/update/account-username")
    public String updateUsername(
            @RequestParam(required = true) String password,
            @RequestParam String username,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        boolean success = false;
        Optional<UserAccount> account = userAccountService
                .findUserAccountByUsername(getAuthenticatedUser().getName());

        if (checkIfAuthenticated(password, account.get().getPassword())) {
            String errorMessage = "";
            try {
                success = userAccountService.updateUsername(account, username);
                refreshAuth(username, password, request);
            } catch (Exception e) {
                errorMessage = e.getMessage();
            }
            return redirectOnSuccess(
                    success, redirectAttributes, "Käyttäjätunnus päivitetty",
                    errorMessage);

        }
        redirectAttributes.addFlashAttribute("basicInfoNotUpdated", "Väärä salasana!");
        return redirectByUserRole();

    }

    /* Delete useraccount */
    @PostMapping("/delete-user")
    public String deleteUserAccount(
            @RequestParam(required = true) String password,
            RedirectAttributes redirectAttributes) {
        boolean deletedSuccessfully = false;
        Optional<UserAccount> account = userAccountService
                .findUserAccountByUsername(getAuthenticatedUser().getName());
        if (checkIfAuthenticated(password, account.get().getPassword())) {
            String errorMessage = "";
            try {
                deletedSuccessfully = userAccountService.deleteAccount(account.get());
            } catch (Exception e) {
                System.out.println("Tiliä ei löytynyt: " + e.getMessage());
                errorMessage = e.getMessage();
            }
            return redirectOnSuccess(
                    deletedSuccessfully, redirectAttributes, "Poistettu!",
                    errorMessage);

        }
        redirectAttributes.addFlashAttribute("basicInfoNotUpdated", "Väärä salasana!");
        return redirectByUserRole();

    }

    /* Get auth */
    private Authentication getAuthenticatedUser() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /* Check password match */
    private boolean checkIfAuthenticated(String inputPassword, String existingPassword) {
        return passwordEncoder.matches(inputPassword, existingPassword);
    }

    /* Refresh session */
    private void refreshAuth(
            String username,
            String password,
            HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(username,
                password);
        Authentication auth = authManager.authenticate(authReq);

        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);

    }

    private String redirectByUserRole() {
        Optional<UserAccount> userAccount = userAccountService
                .findUserAccountByUsername(getAuthenticatedUser().getName());
        if (userAccount.isPresent()) {
            String userRole = userAccount.get().getUserRole();
            return userRole.equals("ROLE_SANTA") ? "redirect:/santa-profile"
                    : userRole.equals("ROLE_CUSTOMER") ? "redirect:/customer-profile" : "redirect:/";
        }
        return "redirect:/custom-logout";
    }

    private String redirectOnSuccess(
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
