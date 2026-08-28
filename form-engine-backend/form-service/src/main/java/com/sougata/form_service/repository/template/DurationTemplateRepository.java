package com.sougata.form_service.repository.template;

import com.sougata.form_service.model.template.DurationTemplate;
import org.springframework.stereotype.Repository;

@Repository("DURATION_TEMPLATE_REPOSITORY")
public interface DurationTemplateRepository extends AnyTypeQuestionTemplateRepository<DurationTemplate, Long> {
}