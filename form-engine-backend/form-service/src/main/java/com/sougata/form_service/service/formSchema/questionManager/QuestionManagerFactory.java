package com.sougata.form_service.service.formSchema.questionManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.details.QuestionDetailsDto;
import com.sougata.form_engine.dto.question.schemaRequest.QuestionPutReqDto;
import com.sougata.form_engine.dto.template.questionTemplate.QuestionTemplateDetails;
import com.sougata.form_service.exception.NoQuestionManagerFoundException;
import com.sougata.form_service.model.formSchema.AnyTypeQuestion;
import com.sougata.form_service.service.formSchema.QuestionManager;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QuestionManagerFactory {

    private final ApplicationContext applicationContext;

    public QuestionManagerFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @SuppressWarnings("unchecked")
    public <Q extends AnyTypeQuestion, QAUR extends QuestionPutReqDto, QR extends QuestionDetailsDto, QTD extends QuestionTemplateDetails>
    QuestionManager<Q, QAUR, QR, QTD> get(QuestionType questionType) {
        try {
            return applicationContext.getBean(
                    String.format("%s_QUESTION_MANAGER", questionType.name()),
                    QuestionManager.class
            );
        } catch (BeansException e) {
            throw new NoQuestionManagerFoundException(questionType);
        }
    }

    @SuppressWarnings("unchecked")
    public <Q extends AnyTypeQuestion, QAUR extends QuestionPutReqDto, QR extends QuestionDetailsDto, QTD extends QuestionTemplateDetails>
    List<QuestionManager<Q, QAUR, QR, QTD>> getAll() {

        List<QuestionManager<Q, QAUR, QR, QTD>> repos = new ArrayList<>();

        for (QuestionType questionType : QuestionType.values()) {
            var repo = applicationContext.getBean(
                    String.format("%s_QUESTION_MANAGER", questionType.name()),
                    QuestionManager.class
            );

            repos.add(repo);
        }

        return repos;
    }

}
