package com.indexzero.santaService.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.indexzero.santaService.model.SantaProfile;
import com.indexzero.santaService.model.UserAccount;
import com.indexzero.santaService.services.SantaProfileService;
import com.indexzero.santaService.services.UserAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//@RequestMapping("/santa")
@Controller
public class SantaProfileController {

    @Autowired
    private SantaProfileService santaProfileService;

    @Autowired
    private UserAccountService userAccountService;

    /* Show actual profile page with contextual content per user: */
    // @Secured("ROLE_SANTA")
    @GetMapping("/santa-profile")
    public String santaProfileView(Model model) {
        Authentication auth = getAuthenticatedUser();
        if (auth.isAuthenticated()) {
            Optional<UserAccount> user = userAccountService.findUserAccountByUsername(auth.getName());
            if (user.isPresent()) {
                model.addAttribute("basicInfo", user.get());
                model.addAttribute("profileInfo", user.get().getSantaProfile());
                if (user.get().getSantaProfile().isAvailable()) {
                    model.addAttribute("checkedYes", "true");
                    model.addAttribute("checkedNo", "false");
                } else {
                    model.addAttribute("checkedYes", "false");
                    model.addAttribute("checkedNo", "true");
                }
            }
            return "santa-profile";
        }

        return "redirect:/login-page";

    }
    /* Get profile image */
    @ResponseBody
    @GetMapping("/santa/image/{id}")
    public byte[] getImage(@PathVariable Long id) {
        return santaProfileService.getSantaprofileImage(id);
    }

    /* get santas */
    @ResponseBody
    @RequestMapping(value = "santas/available", method = RequestMethod.GET, produces = "application/json")
    public List<SantaProfile> getAllAvailableSantas() {
        return santaProfileService.getAvailableSantas();
    }

    /* update santa profile */
    @PostMapping("/update/santa-account")
    public String updateSantaProfile(
            @RequestParam("image") MultipartFile file,
            @RequestParam String profilename,
            @RequestParam String info,
            @RequestParam int price,
            @RequestParam int available,
            RedirectAttributes redirectAttributes) throws IOException{
        /* get useraccount */
        Optional<UserAccount> userAccount = userAccountService
                .findUserAccountByUsername(getAuthenticatedUser().getName());
        /* get accounts santaprofile */
        SantaProfile existingSantaProfile = userAccount.get().getSantaProfile();
        /* Updated info */
        SantaProfile updatedSantaProfile = new SantaProfile();
        if (file.getContentType().equals("image/png") || file.getContentType().equals("image/jpeg")) {
            updatedSantaProfile.setProfileImage(file.getBytes());
        }
        updatedSantaProfile.setSantaProfileName(profilename);
        updatedSantaProfile.setInfo(info);
        updatedSantaProfile.setPrice(price);
        if (available == 0) {
            updatedSantaProfile.setAvailable(false);
        } else if (available == 1) {
            updatedSantaProfile.setAvailable(true);
        }

        santaProfileService.updateSantaProfileInfo(
                userAccount.get(), existingSantaProfile, updatedSantaProfile);

        redirectAttributes.addFlashAttribute("basicInfoUpdated", "Päivitetty");
        //redirectAttributes.addFlashAttribute("basicInfoNotUpdated", "Ei päivitetty");
        return redirectByUserRole();
    }

    /*  */
    private Authentication getAuthenticatedUser() {
        return SecurityContextHolder.getContext().getAuthentication();

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

}
