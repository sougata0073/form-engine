package com.sougata.form_service.repository.template;

import com.sougata.form_service.model.template.DropdownTemplate;
import org.springframework.stereotype.Repository;

@Repository("DROPDOWN_TEMPLATE_REPOSITORY")
public interface DropdownTemplateRepository extends AnyTypeQuestionTemplateRepository<DropdownTemplate, Long> {
}