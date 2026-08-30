package com.sougata.form_service.service.template.questionTemplateManager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.template.questionTemplate.ShortAnswerTemplateDetails;
import com.sougata.form_engine.dto.validation.config.ValidationConfig;
import com.sougata.form_engine.util.JsonUtil;
import com.sougata.form_service.exception.JsonParsingException;
import com.sougata.form_service.model.template.ShortAnswerTemplate;
import com.sougata.form_service.service.template.QuestionTemplateManager;
import org.springframework.stereotype.Service;

@Service("SHORT_ANSWER_TEMPLATE_MANAGER")
public class ShortAnswerTemplateManager extends QuestionTemplateManager<ShortAnswerTemplate, ShortAnswerTemplateDetails> {

    @Override
    public ShortAnswerTemplateDetails toQuestionTemplateDetails(ShortAnswerTemplate template) {
        var sa = new ShortAnswerTemplateDetails();

        populateCommonFields(template, sa);

        try {
            sa.setValidationConfig(
                    JsonUtil.oldJsonNodeToObject(template.getValidationConfig(), ValidationConfig.class)
            );
        } catch (JsonProcessingException e) {
            throw new JsonParsingException(JsonUtil.oldJsonNodeToString(template.getValidationConfig()));
        }

        return sa;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.SHORT_ANSWER;
    }
}
