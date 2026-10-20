package com.sougata.form_data_service.feignClient;

import com.sougata.form_engine.dto.form.FormResponseCountDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(value = "form-response-service", path = "api/v1/form-responses")
public interface FormResponseServiceFeignClient {

    @GetMapping(path = "{formId}/form-response-count")
    FormResponseCountDto getFormResponseCount(@PathVariable("formId") UUID formId);

}
