package com.sougata.form_response_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
public class FormResponseServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FormResponseServiceApplication.class, args);
    }

}
