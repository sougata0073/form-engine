package com.sougata.form_service.service.template;

import com.sougata.form_engine.dto.template.TemplateDetails;
import com.sougata.form_engine.dto.template.TemplateSummaryDto;

import java.util.List;
import java.util.UUID;

public interface TemplateServiceCached {
    List<TemplateSummaryDto> getRecentlyUsedTemplates(UUID userId);

    TemplateDetails getTemplateDetails(Long templateId);

    TemplateDetails loadTemplateDetailsFromDb(Long templateId);
}
