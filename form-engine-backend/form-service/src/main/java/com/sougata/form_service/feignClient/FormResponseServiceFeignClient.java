package com.sougata.form_service.feignClient;

import com.sougata.form_engine.dto.form.FormResponseCountDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient("form-response-service")
public interface FormResponseServiceFeignClient {

    @GetMapping(path = "api/v1/forms/{formId}/form-response-count")
    FormResponseCountDto getFormResponseCount(@PathVariable("formId") UUID formId);

    @GetMapping(path = "api/v1/forms/{formId}/is-response-already-submitted", params = "userId")
    boolean getIsResponseAlreadySubmitted(
            @PathVariable("formId") UUID formId,
            @RequestParam("userId") UUID userId
    );

}
