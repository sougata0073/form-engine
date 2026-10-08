package com.sougata.form_service.feignClient;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("form-data-service")
public interface FormDataServiceFeignClient {

}
