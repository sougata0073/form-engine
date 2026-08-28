package com.sougata.form_service.repository.template;

import com.sougata.form_service.model.template.CheckboxTemplate;
import org.springframework.stereotype.Repository;

@Repository("CHECKBOX_TEMPLATE_REPOSITORY")
public interface CheckboxTemplateRepository extends AnyTypeQuestionTemplateRepository<CheckboxTemplate, Long> {
}