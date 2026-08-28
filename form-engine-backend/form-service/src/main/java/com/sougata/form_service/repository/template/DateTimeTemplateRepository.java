package com.sougata.form_service.repository.template;

import com.sougata.form_service.model.template.DateTimeTemplate;
import org.springframework.stereotype.Repository;

@Repository("DATE_TIME_TEMPLATE_REPOSITORY")
public interface DateTimeTemplateRepository extends AnyTypeQuestionTemplateRepository<DateTimeTemplate, Long> {
}