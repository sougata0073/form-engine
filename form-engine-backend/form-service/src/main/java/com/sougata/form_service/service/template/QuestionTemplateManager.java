package com.sougata.form_service.service.template;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.template.questionTemplate.QuestionTemplateDetails;
import com.sougata.form_service.model.template.AnyTypeQuestionTemplate;

public abstract class QuestionTemplateManager<QT extends AnyTypeQuestionTemplate, QTD extends QuestionTemplateDetails> {

    public abstract QTD toQuestionTemplateDetails(QT template);

    public abstract QuestionType getQuestionType();

    public void populateCommonFields(QT questionTemplate, QTD questionTemplateDetails) {
        questionTemplateDetails.setId(questionTemplate.getQuestionTemplateId());
        questionTemplateDetails.setQuestion(questionTemplate.getQuestionTemplate().getQuestion());
        questionTemplateDetails.setDescription(questionTemplate.getQuestionTemplate().getDescription());
        questionTemplateDetails.setQuestionType(getQuestionType());
        questionTemplateDetails.setRequired(questionTemplate.getQuestionTemplate().getRequired());
        questionTemplateDetails.setOrderIndex(questionTemplate.getQuestionTemplate().getOrderIndex());
    }
}
