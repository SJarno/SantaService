package com.indexzero.santaService.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.indexzero.santaService.model.CustomerProfile;
import com.indexzero.santaService.model.SantaProfile;
import com.indexzero.santaService.model.UserAccount;
import com.indexzero.santaService.repositories.CustomerProfileRepository;
import com.indexzero.santaService.repositories.UserAccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAccountService {

    @Autowired
    private SantaProfileService santaProfileService;

    @Autowired
    private CustomerProfileRepository customerProfileRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityContextService securityContextService;


    @Transactional
    public void createUserAccount(UserAccount userAccount, String role) throws Exception {
        if (usernameExists(userAccount.getUsername())) {
            throw new Exception("Tämä käyttäjätunnus on jo olemassa!");

        }
        if (role.equals("santa")) {
            /* Create useraccount with role santa */
            userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));
            userAccount.setUserRole("ROLE_SANTA");
            SantaProfile santaProfile = new SantaProfile();
            santaProfile.setAvailable(true);
            santaProfile.setContactEmail(userAccount.getEmail());
            santaProfile.setSantaProfileName(userAccount.getFirstName() + " Pukki");
            userAccount.setSantaProfile(santaProfile);

            santaProfileService.saveSantaProfile(santaProfile);
            userAccountRepository.saveAndFlush(userAccount);

        }
        if (role.equals("customer")) {
            userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));
            userAccount.setUserRole("ROLE_CUSTOMER");
            CustomerProfile customerProfile = new CustomerProfile();
            customerProfile.setCustomerProfileName(userAccount.getFirstName());
            customerProfile.setDeliveryAddress(userAccount.getAddress());
            customerProfile.setPostalCode(userAccount.getPostalCode());
            customerProfile.setEmail(userAccount.getEmail());
            userAccount.setCustomerProfile(customerProfile);
            customerProfileRepository.save(customerProfile);
            userAccountRepository.saveAndFlush(userAccount);
        }
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

    /* Update basic info: */
    @Transactional
    public boolean updateAccountInfo(UserAccount newAccount) {
        Optional<UserAccount> oldAccount = securityContextService.getAuthenticatedUserAccount();
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

    /* Update username */
    @Transactional
    public boolean updateUsername(String username) throws Exception {
        if (username.length() < 3 || username.isBlank()) {
            throw new IllegalArgumentException("Tunnus on liian lyhyt!");
        }
        Optional<UserAccount> account = securityContextService.getAuthenticatedUserAccount();
        if (usernameExists(username)) {
            throw new Exception("Tämä käyttäjätunnus on jo olemassa!");

        }
        account.get().setUsername(username);
        return true;

    }

    /* Change password */
    @Transactional
    public boolean changePassword(String oldPassword, String newPassword) {
        if (newPassword.length() < 3 || newPassword == null) {
            throw new IllegalArgumentException("Salasana liian lyhyt!");
        }
        if (securityContextService.accountExistsAndPasswordMatches(oldPassword)) {
            Optional<UserAccount> account = securityContextService.getAuthenticatedUserAccount();
            account.get().setPassword(passwordEncoder.encode(newPassword));
            return true;
        }
        return false;
    }

    /* Delete useraccount and attachded profile */
    @Transactional
    public boolean deleteAccount(UserAccount userAccount) throws NullPointerException {
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
