package com.sougata.form_service.repository.template;

import com.sougata.form_service.model.template.ParagraphTemplate;
import org.springframework.stereotype.Repository;

@Repository("PARAGRAPH_TEMPLATE_REPOSITORY")
public interface ParagraphTemplateRepository extends AnyTypeQuestionTemplateRepository<ParagraphTemplate, Long> {
}