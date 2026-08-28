package com.sougata.auth_service.service;

import com.sougata.auth_service.constant.AuthProvider;
import com.sougata.auth_service.model.UserPrincipal;
import com.sougata.auth_service.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserPrincipal loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmailAndAuthProvider(email, AuthProvider.EMAIL_PASSWORD)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found, Email: " + email));

        return new UserPrincipal(user);
    }

}
