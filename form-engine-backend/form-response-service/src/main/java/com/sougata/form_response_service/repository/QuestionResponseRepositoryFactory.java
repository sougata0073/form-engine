package com.sougata.form_response_service.repository;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.exception.NoQuestionResponseRepositoryFoundException;
import com.sougata.form_response_service.model.AnyTypeQuestionResponse;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QuestionResponseRepositoryFactory {

    private final ApplicationContext applicationContext;

    @Autowired
    public QuestionResponseRepositoryFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

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

    @SuppressWarnings("unchecked")
    public <Q extends AnyTypeQuestionResponse, ID> List<AnyTypeQuestionResponseRepository<Q, ID>> getAll() {
        List<AnyTypeQuestionResponseRepository<Q, ID>> repos = new ArrayList<>();

        for (QuestionType questionType : QuestionType.values()) {
            var repo = applicationContext.getBean(
                    String.format("%s_RESPONSE_REPOSITORY", questionType.name()),
                    AnyTypeQuestionResponseRepository.class
            );

            repos.add(repo);
        }

        return repos;
    }

}
