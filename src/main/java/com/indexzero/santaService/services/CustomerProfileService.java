package com.indexzero.santaService.services;

import java.util.Optional;

import javax.transaction.Transactional;

import com.indexzero.santaService.model.UserAccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerProfileService {


    @Autowired
    private SecurityContextService securityContextService;

    @Transactional
    public boolean updateCustomerProfile(
        String profileName,
        String email,
        String deliveryAddress,
        String postalCode
    ) {
        if (profileName.length() < 3 || profileName.isBlank()) {
            throw new IllegalArgumentException("Profiilinimi on liian lyhyt");
        }
        if (deliveryAddress.isBlank()) {
            throw new IllegalArgumentException("Toimitusosoite ei saa olla tyhjä");
        }
        Optional<UserAccount> userAccount = securityContextService.getAuthenticatedUserAccount();
        if (userAccount.isPresent()) {
            userAccount.get().getCustomerProfile().setCustomerProfileName(profileName);
            userAccount.get().getCustomerProfile().setEmail(email);
            userAccount.get().getCustomerProfile().setDeliveryAddress(deliveryAddress);
            userAccount.get().getCustomerProfile().setPostalCode(postalCode);
            return true;
        }
        return false;
    }
    
}
