package com.sougata.form_service.service.template.questionTemplateManager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sougata.form_service.constant.QuestionType;
import com.sougata.form_service.dto.template.questionTemplate.ParagraphTemplateDetails;
import com.sougata.form_service.exception.JsonParsingException;
import com.sougata.form_service.model.template.ParagraphTemplate;
import com.sougata.form_service.service.template.QuestionTemplateManager;
import com.sougata.form_service.util.JsonUtil;
import com.sougata.form_service.validation.configuration.ValidationConfig;
import org.springframework.stereotype.Service;

@Service("PARAGRAPH_TEMPLATE_MANAGER")
public class ParagraphTemplateManager extends QuestionTemplateManager<ParagraphTemplate, ParagraphTemplateDetails> {

    @Override
    public ParagraphTemplateDetails toQuestionTemplateDetails(ParagraphTemplate template) {
        var p = new ParagraphTemplateDetails();

        populateCommonFields(template, p);

        try {
            p.setValidationConfig(
                    JsonUtil.oldJsonNodeToObject(template.getValidationConfig(), ValidationConfig.class)
            );
        } catch (JsonProcessingException e) {
            throw new JsonParsingException(JsonUtil.oldJsonNodeToString(template.getValidationConfig()));
        }

        return p;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.PARAGRAPH;
    }
}
