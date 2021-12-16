package com.indexzero.santaService.services;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.indexzero.santaService.model.UserAccount;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SecurityContextService {

    private UserAccountService userAccountService;

    private PasswordEncoder passwordEncoder;


    private AuthenticationManager authManager;

    public Optional<UserAccount> getAuthenticatedUserAccount() {
        Optional<UserAccount> userAccount = userAccountService
                .findUserAccountByUsername(getAuthenticatedUser().getName());
        if (userAccount.isPresent()) {
            return userAccount;
        }
        throw new UsernameNotFoundException("Error in authentication!");
    }

    private Authentication getAuthenticatedUser() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
    private boolean checkIfAuthenticated(String inputPassword, String existingPassword) {
        return passwordEncoder.matches(inputPassword, existingPassword);
    }
    public boolean accountExistsAndPasswordMatches(String inputPassword) {
        Optional<UserAccount> user = getAuthenticatedUserAccount();
        return user.isPresent() && checkIfAuthenticated(inputPassword, user.get().getPassword());
    }
    public void refreshAuth(
            String username,
            String password,
            HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(username,
                password);
        Authentication auth = authManager.authenticate(authReq);

        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);

    }

}
