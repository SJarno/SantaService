package com.indexzero.santaService.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.indexzero.santaService.model.Order;
import com.indexzero.santaService.model.OrderStatus;
import com.indexzero.santaService.model.SantaProfile;
import com.indexzero.santaService.model.UserAccount;
import com.indexzero.santaService.services.OrderService;
import com.indexzero.santaService.services.RedirectService;
import com.indexzero.santaService.services.SantaProfileService;
import com.indexzero.santaService.services.SecurityContextService;
import com.indexzero.santaService.services.UserAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SantaProfileController {

    @Autowired
    private SantaProfileService santaProfileService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SecurityContextService securityContextService;

    @Autowired
    private RedirectService redirectService;

    /* Get basic info */
    @GetMapping("/santa-profile")
    public String santaProfileView(Model model) {
        if (securityContextService.getAuthenticatedUser().isAuthenticated()) {
            Optional<UserAccount> user = securityContextService.getAuthenticatedUserAccount();
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

    /* get orders */
    @ResponseBody
    @GetMapping("/santa/orders")
    public List<Order> getSantaOrders() {
        return orderService.getOrdersBySantaprofile();
    }

    /* Update orders */
    @ResponseBody
    @PutMapping("/santa/orders/update/{id}/{status}")
    public Order updateOrderStatus(@PathVariable Long id, @PathVariable OrderStatus status) {
        return orderService.updateStatus(id, status);
    }

    /* update santa profile */
    @PostMapping("/update/santa-account")
    public String updateSantaProfile(
            @RequestParam("image") MultipartFile image,
            @RequestParam String profilename,
            @RequestParam String info,
            @RequestParam int price,
            @RequestParam boolean available,
            @RequestParam String email,
            RedirectAttributes redirectAttributes) throws IOException {
        
        /* Updated info */
        SantaProfile updatedSantaProfile = new SantaProfile();
        updatedSantaProfile.setSantaProfileName(profilename);
        updatedSantaProfile.setInfo(info);
        updatedSantaProfile.setPrice(price);
        updatedSantaProfile.setContactEmail(email);
        updatedSantaProfile.setAvailable(available);
        boolean success = false;
        String errorMessage = "";
        try {
            success = santaProfileService.updateSantaProfileInfo(updatedSantaProfile, image);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }

        return redirectService.redirectOnSuccess(success, redirectAttributes, "Päivitetty", errorMessage);
    }

}
