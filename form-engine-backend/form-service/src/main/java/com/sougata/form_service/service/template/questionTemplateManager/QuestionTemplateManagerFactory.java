package com.sougata.form_service.service.template.questionTemplateManager;

import com.sougata.form_service.constant.QuestionType;
import com.sougata.form_service.dto.template.questionTemplate.QuestionTemplateDetails;
import com.sougata.form_service.exception.NoQuestionTemplateManagerFoundException;
import com.sougata.form_service.model.template.AnyTypeQuestionTemplate;
import com.sougata.form_service.service.template.QuestionTemplateManager;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class QuestionTemplateManagerFactory {

    private final ApplicationContext applicationContext;

    public QuestionTemplateManagerFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @SuppressWarnings("unchecked")
    public <QT extends AnyTypeQuestionTemplate, QTD extends QuestionTemplateDetails> QuestionTemplateManager<QT, QTD> get(QuestionType questionType) {
        try {
            return applicationContext.getBean(
                    String.format("%s_TEMPLATE_MANAGER", questionType.name()),
                    QuestionTemplateManager.class
            );
        } catch (BeansException e) {
            throw new NoQuestionTemplateManagerFoundException(questionType);
        }
    }
}
