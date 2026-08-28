package com.sougata.auth_service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    private final String[] publicUrls = {
            "/api/v1/auth/**",
            "/actuator/**",
            "/login",
            "/login/oauth2/code/**",
            "/oauth2/**",
            "/error",
            "/favicon.ico",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-resources/**",
            "/webjars/**",
            "/api/v1/users/**"
    };

    @Autowired
    public SecurityConfiguration(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService, OAuth2SuccessHandler oAuth2SuccessHandler) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity sec) {
        return sec
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(cus ->
                        cus.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(cus ->
                        cus.requestMatchers(publicUrls).permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2Login(auth ->
                        auth.successHandler(this.oAuth2SuccessHandler)
                )
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var authenticationProvider = new DaoAuthenticationProvider(userDetailsService);

        authenticationProvider.setPasswordEncoder(passwordEncoder);
//        authenticationProvider.setHideUserNotFoundExceptions(false);

        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
