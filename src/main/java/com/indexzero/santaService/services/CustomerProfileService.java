package com.indexzero.santaService.services;

import com.indexzero.santaService.repositories.CustomerProfileRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerProfileService {

    @Autowired
    private CustomerProfileRepository customerProfileRepository;

    
    
}
