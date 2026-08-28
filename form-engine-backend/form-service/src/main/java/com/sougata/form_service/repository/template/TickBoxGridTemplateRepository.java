package com.sougata.form_service.repository.template;

import com.sougata.form_service.model.template.TickBoxGridTemplate;
import org.springframework.stereotype.Repository;

@Repository("TICK_BOX_GRID_TEMPLATE_REPOSITORY")
public interface TickBoxGridTemplateRepository extends AnyTypeQuestionTemplateRepository<TickBoxGridTemplate, Long> {
}