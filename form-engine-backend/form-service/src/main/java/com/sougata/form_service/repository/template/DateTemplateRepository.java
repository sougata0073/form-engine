package com.sougata.form_service.repository.template;

import com.sougata.form_service.model.template.DateTemplate;
import org.springframework.stereotype.Repository;

@Repository("DATE_TEMPLATE_REPOSITORY")
public interface DateTemplateRepository extends AnyTypeQuestionTemplateRepository<DateTemplate, Long> {
}