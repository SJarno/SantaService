package com.indexzero.santaService.controller;

import java.util.List;
import java.util.Optional;

import com.indexzero.santaService.model.Order;
import com.indexzero.santaService.model.UserAccount;
import com.indexzero.santaService.services.CustomerProfileService;
import com.indexzero.santaService.services.OrderService;
import com.indexzero.santaService.services.RedirectService;
import com.indexzero.santaService.services.SecurityContextService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CustomerProfileController {

    @Autowired
    private CustomerProfileService customerProfileService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedirectService redirectService;

    @Autowired
    private SecurityContextService securityContextService;

    @GetMapping("/customer-profile")
    public String customerProfileView(Model model) {
        if (securityContextService.getAuthenticatedUser().isAuthenticated()) {
            /* Get useraccount */
            Optional<UserAccount> userAccount = securityContextService.getAuthenticatedUserAccount();
            if (userAccount.isPresent()) {
                model.addAttribute("basicInfo", userAccount.get());
                model.addAttribute("profileInfo", userAccount.get().getCustomerProfile());
                return "customer-profile";
            }
        }

        return "redirect:login-page";
    }

    /* Update profile-info */
    @PostMapping("/update/customer-profile")
    public String updateCustomerProfile(
            @RequestParam String profileName,
            @RequestParam String email,
            @RequestParam String deliveryAddress,
            @RequestParam String postalCode,
            RedirectAttributes redirectAttributes) {

        boolean success = false;
        String errorMessage = "";
        try {
            success = customerProfileService.updateCustomerProfile(
                    profileName,
                    email,
                    deliveryAddress,
                    postalCode);
        } catch (Exception e) {
            errorMessage = e.getMessage();

        }
        return redirectService.redirectOnSuccess(success, redirectAttributes, "Päivitetty", errorMessage);
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

}
