package com.sougata.form_service.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(value = "form-data-service", path = "api/v1/form-data")
public interface FormDataServiceFeignClient {

    @GetMapping(path = "{formId}/responders/{userId}/is-response-submitted")
    boolean getIsResponseAlreadySubmitted(
            @PathVariable("formId") UUID formId,
            @PathVariable("userId") UUID userId
    );

}
