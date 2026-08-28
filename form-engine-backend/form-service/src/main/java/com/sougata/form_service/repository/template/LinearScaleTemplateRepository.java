package com.sougata.form_service.repository.template;

import com.sougata.form_service.model.template.LinearScaleTemplate;
import org.springframework.stereotype.Repository;

@Repository("LINEAR_SCALE_TEMPLATE_REPOSITORY")
public interface LinearScaleTemplateRepository extends AnyTypeQuestionTemplateRepository<LinearScaleTemplate, Long> {
}