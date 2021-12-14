package com.indexzero.santaService.repositories;


import java.util.List;

import com.indexzero.santaService.model.CustomerProfile;
import com.indexzero.santaService.model.Order;
import com.indexzero.santaService.model.SantaProfile;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>{

    List<Order> findByCustomerProfile(CustomerProfile customerProfile);
    List<Order> findBySantaProfile(SantaProfile santaProfile);

    List<Order> findBySantaProfileId(Long id);
    
    
}
