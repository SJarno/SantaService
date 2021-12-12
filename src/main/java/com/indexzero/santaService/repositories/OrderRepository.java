package com.indexzero.santaService.repositories;


import com.indexzero.santaService.model.Order;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>{
    
}
