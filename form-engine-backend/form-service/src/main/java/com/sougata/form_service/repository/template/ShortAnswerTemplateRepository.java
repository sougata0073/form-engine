package com.sougata.form_service.repository.template;

import com.sougata.form_service.model.template.ShortAnswerTemplate;
import org.springframework.stereotype.Repository;

@Repository("SHORT_ANSWER_TEMPLATE_REPOSITORY")
public interface ShortAnswerTemplateRepository extends AnyTypeQuestionTemplateRepository<ShortAnswerTemplate, Long> {
}