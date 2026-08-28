package com.sougata.auth_service.config;

import com.sougata.auth_service.constant.AuthProvider;
import com.sougata.auth_service.model.User;
import com.sougata.auth_service.repository.UserRepository;
import com.sougata.auth_service.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${frontend-url}")
    private String frontendUrl;

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Autowired
    public OAuth2SuccessHandler(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication
    ) throws IOException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        if (oAuth2User == null) {
            return;
        }

        AuthProvider authProvider = AuthProvider.valueOf(oauthToken.getAuthorizedClientRegistrationId().toUpperCase());

        var extractedUser = extractOAuth2UserAuthAttributes(oAuth2User, authProvider);
        var prevUser = userRepository.findBySocialAuthIdAndAuthProvider(extractedUser.getSocialAuthId(), authProvider);

        var savedUser = prevUser.orElseGet(() -> userRepository.save(extractedUser));

        String jwt = jwtService.generateJwtToken(
                savedUser.getId().toString(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getAvatarUrl()
        );

        String redirectUrl = this.frontendUrl + "/social-auth-success?jwt=" + jwt;

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    private User extractOAuth2UserAuthAttributes(OAuth2User user, AuthProvider authProvider) {
        switch (authProvider) {
            case GOOGLE -> {
                User u = new User();

                u.setSocialAuthId(user.getAttribute("sub").toString());
                u.setUsername(user.getAttribute("name"));
                u.setEmail(user.getAttribute("email"));
                u.setIsEmailVerified(user.getAttribute("email_verified"));
                u.setAvatarUrl(user.getAttribute("picture"));
                u.setAuthProvider(authProvider);

                return u;
            }
            case GITHUB -> {
                User u = new User();

                u.setSocialAuthId(user.getAttribute("id").toString());
                u.setUsername(user.getAttribute("login"));
                u.setEmail(user.getAttribute("email"));
                u.setIsEmailVerified(false);
                u.setAvatarUrl(user.getAttribute("avatar_url"));
                u.setAuthProvider(authProvider);

                return u;
            }
            default -> throw new IllegalArgumentException("Invalid auth provider");
        }
    }
}
