package com.indexzero.santaService.controller;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.indexzero.santaService.model.CustomerProfile;
import com.indexzero.santaService.model.Order;
import com.indexzero.santaService.model.UserAccount;
import com.indexzero.santaService.repositories.CustomerProfileRepository;
import com.indexzero.santaService.repositories.OrderRepository;
import com.indexzero.santaService.services.CustomerProfileService;
import com.indexzero.santaService.services.OrderService;
import com.indexzero.santaService.services.UserAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CustomerProfileController {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private OrderService orderService;


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
    /* Update profile-info */
    @Transactional
    @PostMapping("/update/customer-profile")
    public String updateCustomerProfile(
        @RequestParam String profileName,
        @RequestParam String email,
        @RequestParam String deliveryAddress,
        @RequestParam String postalCode
        ) {
        Optional<UserAccount> userAccount = userAccountService
                .findUserAccountByUsername(getAuthenticatedUser().getName());
        
        System.out.println(profileName);
        if (userAccount.isPresent()) {
            userAccount.get().getCustomerProfile().setCustomerProfileName(profileName);
            userAccount.get().getCustomerProfile().setEmail(email);
            userAccount.get().getCustomerProfile().setDeliveryAddress(deliveryAddress);
            userAccount.get().getCustomerProfile().setPostalCode(postalCode);
        }
        return redirectByUserRole();
    }
    /* Create new order */
    @ResponseBody
    @PostMapping("/customer/{id}/create-order")
    public Order createOrder(@PathVariable Long id) {
        return orderService.createOrder(id);
    }
    /* Get orders made by customer: */
    @ResponseBody
    @GetMapping("/customer/orders")
    public List<Order> getAllOrdersAndSantaprofiles() {

        return orderService.getOrdersByCustomerProfile();
    }
    /* Delete order */
    @ResponseBody
    @DeleteMapping("/orders/{id}/delete")
    public Order deleteOrderById(@PathVariable Long id) {
        return orderService.deleteOrder(id);
    }
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
