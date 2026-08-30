package com.sougata.form_service.controller;

import com.sougata.form_engine.constant.cache.TemplateCacheNames;
import com.sougata.form_engine.dto.template.TemplateSummariesDto;
import com.sougata.form_engine.dto.template.TemplateToFormBuildResDto;
import com.sougata.form_service.service.template.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/templates")
@CrossOrigin
public class TemplateController {

    private final TemplateService templateService;

    @Autowired
    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping
    public TemplateSummariesDto getAllTemplateSummaries(@RequestHeader("auth-jwt") UUID userId) {
        return templateService.getAllTemplateSummaries(userId);
    }

    @PostMapping(path = "{templateId}/build-form")
    @CacheEvict(cacheNames = {TemplateCacheNames.RECENTLY_USED_TEMPLATES}, key = "#userId")
    public ResponseEntity<TemplateToFormBuildResDto> buildFormFromTemplate(
            @PathVariable Long templateId,
            @RequestHeader("auth-jwt") UUID userId
    ) {
        var res = templateService.buildFormFromTemplate(templateId, userId);

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

}
