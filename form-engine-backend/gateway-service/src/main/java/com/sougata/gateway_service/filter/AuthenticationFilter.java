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

        var uri = exchange.getRequest().getURI();

        var processedExchange = exchange.mutate()
                .request(request -> request.headers(headers -> {

                    headers.set(
                            HttpHeaderConstant.X_FORWARDED_PROTO,
                            uri.getScheme()
                    );

                    headers.set(
                            HttpHeaderConstant.X_FORWARDED_HOST,
                            uri.getRawAuthority()
                    );

                    int port = uri.getPort();

                    if (port == -1) {
                        headers.remove(HttpHeaderConstant.X_FORWARDED_PORT);
                    } else {
                        headers.set(
                                HttpHeaderConstant.X_FORWARDED_PORT,
                                String.valueOf(port)
                        );
                    }
                }))
                .build();

        var authHeader = processedExchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (processedExchange.getRequest().getMethod() == HttpMethod.OPTIONS || authHeader == null) {
            return chain.filter(processedExchange);
        }

        if (!authHeader.startsWith(AuthConstant.AUTH_HEADER_PREFIX) || authHeader.length() == AuthConstant.AUTH_HEADER_PREFIX.length()) {
            processedExchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return processedExchange.getResponse().setComplete();
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
            processedExchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return processedExchange.getResponse().setComplete();
        }

        var usernamePassAuthToken = UsernamePasswordAuthenticationToken.authenticated(
                userId, null, Collections.emptyList()
        );

        var newRequest = processedExchange
                .getRequest()
                .mutate()
                .header(HttpHeaderConstant.X_USER_ID, userId)
                .build();

        var newExchange = processedExchange.mutate().request(newRequest).build();

        var contextHolder = ReactiveSecurityContextHolder.withAuthentication(usernamePassAuthToken);

        return chain.filter(newExchange).contextWrite(contextHolder);
    }
}
