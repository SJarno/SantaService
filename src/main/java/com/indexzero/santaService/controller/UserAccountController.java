package com.indexzero.santaService.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.indexzero.santaService.model.UserAccount;
import com.indexzero.santaService.services.RedirectService;
import com.indexzero.santaService.services.SecurityContextService;
import com.indexzero.santaService.services.UserAccountService;

import org.springframework.beans.factory.annotation.Autowired;
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
    private RedirectService redirectService;

    @Autowired
    private SecurityContextService securityContextService;

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

        boolean success = false;
        if (securityContextService.accountExistsAndPasswordMatches(password)) {
            UserAccount newAccount = new UserAccount();
            newAccount.setFirstName(firstName);
            newAccount.setLastName(lastName);
            newAccount.setEmail(email);
            newAccount.setPhoneNumber(phonenumber);
            newAccount.setPostalCode(postalCode);
            success = userAccountService.updateAccountInfo(newAccount);
            /* if successful add message and redirect by role */
            return redirectService.redirectOnSuccess(
                    success,
                    redirectAttributes,
                    "Perustiedot päivitetty",
                    "Jotain meni vikaan, perustietoja ei päivietty");

        }
        redirectAttributes.addFlashAttribute("basicInfoNotUpdated", "Väärä salasana!");
        return redirectService.redirectByUserRole();

    }

    /* Update username */
    @PostMapping("/update/account-username")
    public String updateUsername(
            @RequestParam(required = true) String password,
            @RequestParam String username,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        boolean success = false;
        if (securityContextService.accountExistsAndPasswordMatches(password)) {
            String errorMessage = "";
            try {
                success = userAccountService.updateUsername(username);
                /* refreshAuth(username, password, request); */
                securityContextService.refreshAuth(username, password, request);
            } catch (Exception e) {
                errorMessage = e.getMessage();
            }
            return redirectService.redirectOnSuccess(
                    success, redirectAttributes, "Käyttäjätunnus päivitetty", errorMessage);

        }
        redirectAttributes.addFlashAttribute("basicInfoNotUpdated", "Väärä salasana!");
        return redirectService.redirectByUserRole();

    }

    /* Delete useraccount */
    @PostMapping("/delete-user")
    public String deleteUserAccount(
            @RequestParam(required = true) String password,
            RedirectAttributes redirectAttributes) {
        boolean success = false;
        Optional<UserAccount> account = securityContextService.getAuthenticatedUserAccount();
        if (securityContextService.accountExistsAndPasswordMatches(password)) {
            String errorMessage = "";
            try {
                success = userAccountService.deleteAccount(account.get());
            } catch (Exception e) {
                System.out.println("Tiliä ei löytynyt: " + e.getMessage());
                errorMessage = e.getMessage();
            }
            return redirectService.redirectOnSuccess(
                    success, redirectAttributes, "Poistettu", errorMessage);

        }
        redirectAttributes.addFlashAttribute("basicInfoNotUpdated", "Väärä salasana!");
        return redirectService.redirectByUserRole();

    }

}
