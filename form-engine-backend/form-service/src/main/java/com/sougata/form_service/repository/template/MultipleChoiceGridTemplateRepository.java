package com.sougata.form_service.repository.template;

import com.sougata.form_service.model.template.MultipleChoiceGridTemplate;
import org.springframework.stereotype.Repository;

@Repository("MULTIPLE_CHOICE_GRID_TEMPLATE_REPOSITORY")
public interface MultipleChoiceGridTemplateRepository extends AnyTypeQuestionTemplateRepository<MultipleChoiceGridTemplate, Long> {
}