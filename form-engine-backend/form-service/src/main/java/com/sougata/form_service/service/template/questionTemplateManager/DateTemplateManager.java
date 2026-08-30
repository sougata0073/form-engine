package com.sougata.form_service.service.template.questionTemplateManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.template.questionTemplate.DateTemplateDetails;
import com.sougata.form_service.model.template.DateTemplate;
import com.sougata.form_service.service.template.QuestionTemplateManager;
import org.springframework.stereotype.Service;

@Service("DATE_TEMPLATE_MANAGER")
public class DateTemplateManager extends QuestionTemplateManager<DateTemplate, DateTemplateDetails> {

    @Override
    public DateTemplateDetails toQuestionTemplateDetails(DateTemplate template) {
        var d = new DateTemplateDetails();

        populateCommonFields(template, d);

        return d;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.DATE;
    }
}
