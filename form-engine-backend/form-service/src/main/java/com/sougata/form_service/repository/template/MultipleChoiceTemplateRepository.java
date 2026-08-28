package com.sougata.form_service.repository.template;

import com.sougata.form_service.model.template.MultipleChoiceTemplate;
import org.springframework.stereotype.Repository;

@Repository("MULTIPLE_CHOICE_TEMPLATE_REPOSITORY")
public interface MultipleChoiceTemplateRepository extends AnyTypeQuestionTemplateRepository<MultipleChoiceTemplate, Long> {
}