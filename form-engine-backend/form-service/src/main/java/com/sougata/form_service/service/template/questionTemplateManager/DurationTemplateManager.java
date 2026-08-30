package com.sougata.form_service.service.template.questionTemplateManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.template.questionTemplate.DurationTemplateDetails;
import com.sougata.form_service.model.template.DurationTemplate;
import com.sougata.form_service.service.template.QuestionTemplateManager;
import org.springframework.stereotype.Service;

@Service("DURATION_TEMPLATE_MANAGER")
public class DurationTemplateManager extends QuestionTemplateManager<DurationTemplate, DurationTemplateDetails> {

    @Override
    public DurationTemplateDetails toQuestionTemplateDetails(DurationTemplate template) {
        var d = new DurationTemplateDetails();

        populateCommonFields(template, d);

        return d;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.DURATION;
    }
}
