package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.exception.NoQuestionResponseRepositoryFoundException;
import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_engine.constant.QuestionType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionResponseRepositoryFactory {

    private final ApplicationContext applicationContext;

    @SuppressWarnings("unchecked")
    public <Q extends AnyTypeQuestionResponse, ID> AnyTypeQuestionResponseRepository<Q, ID> get(QuestionType questionType) {
        try {
            return applicationContext.getBean(
                    String.format("%s_RESPONSE_REPOSITORY", questionType.name()),
                    AnyTypeQuestionResponseRepository.class
            );
        } catch (BeansException e) {
            throw new NoQuestionResponseRepositoryFoundException(questionType);
        }
    }

}
