package com.sougata.form_service.repository.template;

import com.sougata.form_service.model.template.RatingTemplate;
import org.springframework.stereotype.Repository;

@Repository("RATING_TEMPLATE_REPOSITORY")
public interface RatingTemplateRepository extends AnyTypeQuestionTemplateRepository<RatingTemplate, Long> {
}