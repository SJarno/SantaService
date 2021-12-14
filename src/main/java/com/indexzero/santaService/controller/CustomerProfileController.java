package com.indexzero.santaService.controller;

import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CustomerProfileController {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private OrderService orderService;

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
