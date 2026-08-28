package com.sougata.form_service.repository.template;

import com.sougata.form_service.constant.QuestionType;
import com.sougata.form_service.exception.NoQuestionTemplateRepositoryFoundException;
import com.sougata.form_service.model.template.AnyTypeQuestionTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AnyQuestionTemplateRepositoryFactory {

    private final ApplicationContext applicationContext;

    public AnyQuestionTemplateRepositoryFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @SuppressWarnings("unchecked")
    public <QT extends AnyTypeQuestionTemplate, ID> AnyTypeQuestionTemplateRepository<QT, ID> get(QuestionType questionType) {
        try {
            return applicationContext.getBean(
                    String.format("%s_TEMPLATE_REPOSITORY", questionType.name()),
                    AnyTypeQuestionTemplateRepository.class
            );
        } catch (BeansException e) {
            throw new NoQuestionTemplateRepositoryFoundException(questionType);
        }
    }

}
