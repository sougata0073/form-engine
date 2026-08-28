package com.sougata.form_service.interceptor;

import com.sougata.form_service.util.JsonUtil;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@RestControllerAdvice
public class ResponseInterceptor implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    public ResponseInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public @Nullable Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        JsonNode node = objectMapper.valueToTree(body);

        JsonUtil.removeFieldRecursiveNewJsonNode(node, "@class");

        return node;
    }

}
