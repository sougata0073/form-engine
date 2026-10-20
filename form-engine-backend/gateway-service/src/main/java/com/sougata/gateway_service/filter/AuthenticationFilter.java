package com.sougata.gateway_service.filter;

import com.sougata.form_engine.constant.AuthConstant;
import com.sougata.form_engine.constant.HttpHeaderConstant;
import com.sougata.form_engine.util.JwtUtil;
import com.sougata.gateway_service.configuration.AppConfiguration;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;

@RequiredArgsConstructor
public class AuthenticationFilter implements WebFilter {

    private final AppConfiguration appConfiguration;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        var authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS || authHeader == null) {
            return chain.filter(exchange);
        }

        if (!authHeader.startsWith(AuthConstant.AUTH_HEADER_PREFIX) || authHeader.length() == AuthConstant.AUTH_HEADER_PREFIX.length()) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        var jwt = authHeader.substring(AuthConstant.AUTH_HEADER_PREFIX.length());

        String userId;

        try {
            userId = JwtUtil.extractData(
                    appConfiguration.getJwt().getSecretString(),
                    jwt,
                    Claims::getSubject
            );
        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        var usernamePassAuthToken = UsernamePasswordAuthenticationToken.authenticated(
                userId, null, Collections.emptyList()
        );

        var newRequest = exchange
                .getRequest()
                .mutate()
                .header(HttpHeaderConstant.X_USER_ID, userId)
                .build();

        var newExchange = exchange.mutate().request(newRequest).build();

        var contextHolder = ReactiveSecurityContextHolder.withAuthentication(usernamePassAuthToken);

        return chain.filter(newExchange).contextWrite(contextHolder);
    }
}
