package com.indexzero.santaService.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.indexzero.santaService.model.CustomerProfile;
import com.indexzero.santaService.model.Order;
import com.indexzero.santaService.model.OrderStatus;
import com.indexzero.santaService.model.SantaProfile;
import com.indexzero.santaService.model.UserAccount;
import com.indexzero.santaService.repositories.OrderRepository;

import org.apache.tomcat.jni.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private SantaProfileService santaProfileService;

    /* Create order */
    @Transactional
    public Order createOrder(Long santaId) {
        Optional<UserAccount> authenticatedUser = userAccountService
            .findUserAccountByUsername(getAuthenticatedUser().getName());
        if (authenticatedUser.isPresent()) {
            CustomerProfile customerProfile = authenticatedUser.get().getCustomerProfile();
            SantaProfile santaProfile = santaProfileService.getProfileByid(santaId).get();

            /* Check if customer already has an order with santa */
            /* System.out.println(customerProfile.getOrders()); */
            
            /* Create new order with pending status */
            Order order = new Order();
            OrderStatus status = OrderStatus.PENDING;
            order.setStatus(status);
            order.setSantaProfile(santaProfile);
            order.setCustomerProfile(customerProfile);
            customerProfile.getOrders().add(order);
            santaProfile.getOrders().add(order);
            return orderRepository.saveAndFlush(order);
        }
        return new Order();
    }

    /* Get orders */
    

    /* Get order */
    /* get order by santa id */
    /* get order by customer id */
    /* get order by authenticated customer and santa id */
    public List<Order> getOrders() {
        CustomerProfile customerProfile = userAccountService.findUserAccountByUsername(getAuthenticatedUser().getName()).get().getCustomerProfile();
        return orderRepository.findByCustomerProfile(customerProfile);
    }

    /* Update order */
    /* Updating order status */

    /* Delete order */
    @Transactional
    public Order deleteOrder(Long id) {
        /* Optional<UserAccount> userAccount = userAccountService.findUserAccountByUsername(getAuthenticatedUser().getName()); */
        Optional<Order> orderToDelete = orderRepository.findById(id);
        if (orderToDelete.isPresent()) {
            System.out.println();
            System.out.println("Servicessä rivi 95");
            System.out.println(orderToDelete.get().getId());
            System.out.println();
            /* Remove order from customerprofile */
            
            orderRepository.flush();
            orderRepository.deleteById(id);

            /* santa.setOrders(orders);
            userAccount.get().getCustomerProfile().setOrders(customerOrders); */
            return orderToDelete.get();
        }
        return new Order();
        
    }

    private Authentication getAuthenticatedUser() {
        return SecurityContextHolder.getContext().getAuthentication();

    }
}
