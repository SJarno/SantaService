package com.indexzero.santaService.security;

import java.util.Arrays;
import java.util.Optional;

import com.indexzero.santaService.model.UserAccount;
import com.indexzero.santaService.repositories.UserAccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//@Profile("dev")
@Service
public class CustomUserAccountUserDetailsService implements UserDetailsService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserAccount> customerAccount = userAccountRepository.findByUsername(username);
        if (customerAccount.isPresent()) {
            return new org.springframework.security.core.userdetails.User(
                    customerAccount.get().getUsername(),
                    customerAccount.get().getPassword(),
                    true,
                    true,
                    true,
                    true,
                    Arrays.asList(
                            new SimpleGrantedAuthority(customerAccount.get().getUserRole())));
        }
        throw new UsernameNotFoundException("No such email: " + username);
    }

}
