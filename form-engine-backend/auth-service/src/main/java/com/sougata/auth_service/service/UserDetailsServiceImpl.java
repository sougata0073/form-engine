package com.sougata.auth_service.service;

import com.sougata.auth_service.constant.AuthProvider;
import com.sougata.auth_service.model.UserPrincipal;
import com.sougata.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public @NonNull UserPrincipal loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmailAndAuthProvider(email, AuthProvider.EMAIL_PASSWORD)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found, Email: " + email));

        return new UserPrincipal(user);
    }

}
