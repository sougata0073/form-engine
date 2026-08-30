package com.sougata.form_service.repository.formSchema;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_service.exception.NoQuestionRepositoryFoundException;
import com.sougata.form_service.model.formSchema.AnyTypeQuestion;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnyTypeQuestionRepositoryFactory {

    private final ApplicationContext applicationContext;

    @Autowired
    public AnyTypeQuestionRepositoryFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @SuppressWarnings("unchecked")
    public <T extends AnyTypeQuestion, ID> AnyTypeQuestionRepository<T, ID>
    get(QuestionType questionType) {
        try {
            return applicationContext.getBean(
                    String.format("%s_REPOSITORY", questionType.name()),
                    AnyTypeQuestionRepository.class
            );
        } catch (BeansException e) {
            throw new NoQuestionRepositoryFoundException(questionType);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends AnyTypeQuestion, ID> List<AnyTypeQuestionRepository<T, ID>> getAll() {
        List<AnyTypeQuestionRepository<T, ID>> repos = new ArrayList<>();

        for (QuestionType questionType : QuestionType.values()) {
            var repo = applicationContext.getBean(
                    String.format("%s_REPOSITORY", questionType.name()),
                    AnyTypeQuestionRepository.class
            );

            repos.add(repo);
        }

        return repos;
    }
}
