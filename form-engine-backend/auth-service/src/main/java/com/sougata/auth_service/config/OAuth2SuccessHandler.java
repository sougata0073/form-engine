package com.sougata.auth_service.config;

import com.sougata.auth_service.model.User;
import com.sougata.auth_service.repository.UserRepository;
import com.sougata.auth_service.service.JwtService;
import com.sougata.form_engine.constant.messaging.MessagingChannelNames;
import com.sougata.form_engine.dto.messaging.UserCreatedMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${frontend-url}")
    private String frontendUrl;

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication
    ) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        if (oAuth2User == null) {
            return;
        }

        var extractedUser = new User();

        extractedUser.setGoogleId(oAuth2User.getAttribute("sub").toString());
        extractedUser.setUserName(oAuth2User.getAttribute("name"));
        extractedUser.setEmail(oAuth2User.getAttribute("email"));
        extractedUser.setIsEmailVerified(oAuth2User.getAttribute("email_verified"));
        extractedUser.setAvatarUrl(oAuth2User.getAttribute("picture"));

        var prevUser = userRepository.findByGoogleId(extractedUser.getGoogleId());

        var savedUser = prevUser.orElseGet(() -> userRepository.save(extractedUser));

        if (prevUser.isEmpty()) {
            redisTemplate.convertAndSend(MessagingChannelNames.USER_CREATED, new UserCreatedMessage(savedUser.getId()));
        }

        String jwt = jwtService.generateJwtToken(
                savedUser.getId().toString(), savedUser.getUserName(), savedUser.getEmail(), savedUser.getAvatarUrl()
        );

        String redirectUrl = this.frontendUrl + "/auth-success?jwt=" + jwt;

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

}
