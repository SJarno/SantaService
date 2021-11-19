package com.indexzero.santaservice.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.indexzero.santaservice.model.SantaAccount;
import com.indexzero.santaservice.repositories.SantaAccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SantaAccountService {

    @Autowired
    private SantaAccountRepository santaAccountRepository;

    /* Save account */
    @Transactional
    public void save(SantaAccount santaAccount) {
        Optional<SantaAccount> getByMail = santaAccountRepository.findByEmailEquals(santaAccount.getEmail());
        if (getByMail.isPresent()) {
            throw new IllegalArgumentException("Email taken!");
        }
        santaAccountRepository.save(santaAccount);

    }

    /* Get all santas: */
    public List<SantaAccount> getAllSantas() {
        return santaAccountRepository.findAll();
    }

    /* Include only what needed */
    public List<SantaAccount> getNewSantas() {
        return santaAccountRepository.findAll().stream().map(account -> {
            SantaAccount newAccount = new SantaAccount();
            newAccount.setFirstName(account.getFirstName());
            return newAccount;
        }).collect(Collectors.toList());
    }

}
