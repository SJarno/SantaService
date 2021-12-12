package com.indexzero.santaService.repositories;


import com.indexzero.santaService.model.Order;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Order, Long>{
    
}
