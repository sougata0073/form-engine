package com.sougata.form_service.repository.template;

import com.sougata.form_service.model.template.TimeTemplate;
import org.springframework.stereotype.Repository;

@Repository("TIME_TEMPLATE_REPOSITORY")
public interface TimeTemplateRepository extends AnyTypeQuestionTemplateRepository<TimeTemplate, Long> {
}