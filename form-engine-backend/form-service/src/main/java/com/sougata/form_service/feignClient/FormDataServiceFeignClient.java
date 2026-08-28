package com.sougata.form_service.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient("form-data-service")
public interface FormDataServiceFeignClient {

}
