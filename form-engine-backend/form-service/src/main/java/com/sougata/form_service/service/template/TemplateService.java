package com.sougata.form_service.service.template;

import com.sougata.form_service.dto.template.TemplateSummariesDto;
import com.sougata.form_service.dto.template.TemplateToFormBuildResDto;

import java.util.UUID;

public interface TemplateService {
    TemplateSummariesDto getAllTemplateSummaries(UUID userId);
    TemplateToFormBuildResDto buildFormFromTemplate(Long templateId, UUID userId);
}
