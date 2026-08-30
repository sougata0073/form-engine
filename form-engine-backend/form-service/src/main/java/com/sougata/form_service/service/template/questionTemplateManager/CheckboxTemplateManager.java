package com.sougata.form_service.service.template.questionTemplateManager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.template.questionTemplate.CheckboxTemplateDetails;
import com.sougata.form_engine.dto.validation.config.ValidationConfig;
import com.sougata.form_engine.util.JsonUtil;
import com.sougata.form_service.exception.JsonParsingException;
import com.sougata.form_service.model.template.CheckboxTemplate;
import com.sougata.form_service.service.template.QuestionTemplateManager;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service("CHECKBOX_TEMPLATE_MANAGER")
public class CheckboxTemplateManager extends QuestionTemplateManager<CheckboxTemplate, CheckboxTemplateDetails> {

    @Override
    public CheckboxTemplateDetails toQuestionTemplateDetails(CheckboxTemplate template) {
        var cb = new CheckboxTemplateDetails();

        populateCommonFields(template, cb);

        var options = template.getOptions().stream()
                .map(o ->
                        new CheckboxTemplateDetails.CheckboxOptionTemplateDetails(o.getId(), o.getOption(), o.getOrderIndex())
                )
                .sorted(Comparator.comparingInt(CheckboxTemplateDetails.CheckboxOptionTemplateDetails::getOrderIndex))
                .toList();

        cb.setOptions(options);

        try {
            cb.setValidationConfig(
                    JsonUtil.oldJsonNodeToObject(template.getValidationConfig(), ValidationConfig.class)
            );
        } catch (JsonProcessingException e) {
            throw new JsonParsingException(JsonUtil.oldJsonNodeToString(template.getValidationConfig()));
        }

        return cb;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.CHECKBOX;
    }
}
