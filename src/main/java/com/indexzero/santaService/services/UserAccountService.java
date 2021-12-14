package com.indexzero.santaService.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.indexzero.santaService.model.CustomerProfile;
import com.indexzero.santaService.model.Order;
import com.indexzero.santaService.model.SantaProfile;
import com.indexzero.santaService.model.UserAccount;
import com.indexzero.santaService.repositories.CustomerProfileRepository;
import com.indexzero.santaService.repositories.UserAccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAccountService {

    /*
     * @Autowired
     * private SantaProfileRepository santaProfileRepository;
     */
    @Autowired
    private SantaProfileService santaProfileService;

    @Autowired
    private CustomerProfileRepository customerProfileRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /* Create */
    @Transactional
    public void saveSantaAccount(UserAccount santaAccount) throws Exception {
        /* Check if username exists */
        boolean usernameExists = usernameExists(santaAccount.getUsername());
        if (usernameExists) {
            throw new Exception("Tämä käyttäjätunnus on jo olemassa!");

        }

        santaAccount.setPassword(passwordEncoder.encode(santaAccount.getPassword()));
        santaAccount.setUserRole("ROLE_SANTA");
        SantaProfile santaProfile = new SantaProfile();
        santaAccount.setSantaProfile(santaProfile);

        santaProfileService.saveSantaProfile(santaProfile);
        userAccountRepository.saveAndFlush(santaAccount);

    }

    @Transactional
    public void createCustomerAccount(UserAccount customerAccount) throws Exception {
        boolean usernameExists = usernameExists(customerAccount.getUsername());
        if (usernameExists) {
            throw new Exception("Tämä käyttäjätunnus on jo olemassa!");

        }
        customerAccount.setPassword(passwordEncoder.encode(customerAccount.getPassword()));
        customerAccount.setUserRole("ROLE_CUSTOMER");
        CustomerProfile customerProfile = new CustomerProfile();
        customerAccount.setCustomerProfile(customerProfile);
        customerProfileRepository.save(customerProfile);
        userAccountRepository.saveAndFlush(customerAccount);
    }

    public Optional<UserAccount> findUserAccountById(Long id) {
        return userAccountRepository.findById(id);
    }

    public Optional<UserAccount> findUserAccountByUsername(String username) {
        return userAccountRepository.findByUsername(username);
    }

    /* Read */
    public List<UserAccount> getAllUsers() {
        return userAccountRepository.findAll();
    }

    /* Get Santas by role, and include only what needed */
    public List<UserAccount> getNewSantas() {
        return userAccountRepository.findAll().stream()
                .filter(user -> user.getUserRole().equals("ROLE_SANTA"))
                .map(account -> {
                    UserAccount newAccount = new UserAccount();
                    newAccount.setFirstName(account.getFirstName());
                    return newAccount;
                }).collect(Collectors.toList());
    }

    /* Get all santas, by role: */
    public List<UserAccount> getAllSantaUsers() {
        return userAccountRepository.findAll().stream()
                .filter(user -> user.getUserRole().equals("ROLE_SANTA"))
                .collect(Collectors.toList());
    }

    /* Update basic info: */
    @Transactional
    public boolean updateAccountInfo(Optional<UserAccount> oldAccount, UserAccount newAccount) {
        if (oldAccount.isPresent()) {
            /* Check if any value is blank */
            if (newAccount.anyValueBlank()) {
                return false;
            }
            
            /* Else assign new values to old */
            oldAccount.get().setFirstName(newAccount.getFirstName());
            oldAccount.get().setLastName(newAccount.getLastName());
            oldAccount.get().setEmail(newAccount.getEmail());
            oldAccount.get().setPhoneNumber(newAccount.getPhoneNumber());
            oldAccount.get().setPostalCode(newAccount.getPostalCode());
            return true;
        }
        return false;

    }

    /* Update username */
    @Transactional
    public boolean updateUsername(Optional<UserAccount> account, String username) throws Exception {
        if (account.isPresent()) {
            boolean usernameExists = usernameExists(username);
            if (usernameExists) {
                throw new Exception("Tämä käyttäjätunnus on jo olemassa!");

            }
            account.get().setUsername(username);
            return true;
        }
        return false;
    }

    /* Delete useraccount and attachded profile */
    @Transactional
    public boolean deleteAccount(UserAccount userAccount) throws NullPointerException {
        /* userAccountRepository.delete(userAccount); */
        userAccountRepository.deleteById(userAccount.getId());
        /* If account has certain role */
        if (userAccount.getUserRole().equals("ROLE_SANTA")) {
            Optional<SantaProfile> santaProfileToDelete = santaProfileService
                    .getProfileByid(userAccount
                            .getSantaProfile().getId());
            if (santaProfileToDelete.isPresent()) {
                santaProfileService.deleteSantaprofile(santaProfileToDelete.get().getId());
                
            }

        }
        if (userAccount.getUserRole().equals("ROLE_CUSTOMER")) {
            Optional<CustomerProfile> customerProfileToDelete = customerProfileRepository
                    .findById(userAccount
                            .getCustomerProfile().getId());
            if (customerProfileToDelete.isPresent()) {
                customerProfileRepository.delete(customerProfileToDelete.get());
            }

        }

        return true;
    }


    private boolean usernameExists(String username) {
        return userAccountRepository.findByUsername(username).isPresent();
    }

}
