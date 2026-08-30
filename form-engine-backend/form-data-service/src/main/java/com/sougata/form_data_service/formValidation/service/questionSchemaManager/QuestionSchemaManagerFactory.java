package com.sougata.form_data_service.formValidation.service.questionSchemaManager;

import com.sougata.form_data_service.formValidation.service.QuestionSchemaManager;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.details.QuestionDetailsDto;
import com.sougata.form_engine.dto.question.responseputrequest.QuestionResponsePutReqDto;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QuestionSchemaManagerFactory {

    private final ApplicationContext applicationContext;

    public QuestionSchemaManagerFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @SuppressWarnings("unchecked")
    public <QR extends QuestionDetailsDto, V extends QuestionResponsePutReqDto>
    QuestionSchemaManager<QR, V> get(QuestionType questionType) {
        try {
            return applicationContext.getBean(
                    String.format("%s_QUESTION_SCHEMA_MANAGER", questionType.name()),
                    QuestionSchemaManager.class
            );
        } catch (BeansException e) {
            throw new IllegalArgumentException("No question schema manager found for question type: " + questionType);
        }
    }

    @SuppressWarnings("unchecked")
    public <QR extends QuestionDetailsDto, V extends QuestionResponsePutReqDto>
    List<QuestionSchemaManager<QR, V>> getAll() {

        List<QuestionSchemaManager<QR, V>> repos = new ArrayList<>();

        for (QuestionType questionType : QuestionType.values()) {
            var repo = applicationContext.getBean(
                    String.format("%s_QUESTION_SCHEMA_MANAGER", questionType.name()),
                    QuestionSchemaManager.class
            );

            repos.add(repo);
        }

        return repos;
    }

}
