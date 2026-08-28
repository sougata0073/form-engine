package com.sougata.form_response_service.service.responseManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.ResponseIndividualDto;
import com.sougata.form_engine.dto.formResponse.question.ResponseByQuestionResponse;
import com.sougata.form_engine.dto.formResponse.question.ResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.ResponseSummaryDto;
import com.sougata.form_engine.dto.question.details.QuestionDetailsDto;
import com.sougata.form_engine.exception.NoResponseManagerFoundException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ResponseManagerFactory {

    private final ApplicationContext applicationContext;

    public ResponseManagerFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @SuppressWarnings("unchecked")
    public <
            RS extends ResponseSummaryDto<?>,
            QRes extends QuestionDetailsDto,
            ResByQ extends ResponseQuestionDto<ResByQRes>,
            ResByQRes extends ResponseByQuestionResponse,
            ResIndi extends ResponseIndividualDto
            >
    ResponseManager<RS, QRes, ResByQ, ResByQRes, ResIndi> get(QuestionType questionType) {
        try {
            return applicationContext.getBean(
                    String.format("%s_RESPONSE_MANAGER", questionType.name()),
                    ResponseManager.class
            );
        } catch (BeansException e) {
            throw new NoResponseManagerFoundException(questionType);
        }
    }

    @SuppressWarnings("unchecked")
    public <
            RS extends ResponseSummaryDto<?>,
            QRes extends QuestionDetailsDto,
            ResByQ extends ResponseQuestionDto<ResByQRes>,
            ResByQRes extends ResponseByQuestionResponse,
            ResIndi extends ResponseIndividualDto
            >
    List<ResponseManager<RS, QRes, ResByQ, ResByQRes, ResIndi>> getAll() {
        List<ResponseManager<RS, QRes, ResByQ, ResByQRes, ResIndi>> repos = new ArrayList<>();

        for (QuestionType questionType : QuestionType.values()) {
            var repo = applicationContext.getBean(
                    String.format("%s_RESPONSE_MANAGER", questionType.name()),
                    ResponseManager.class
            );

            repos.add(repo);
        }

        return repos;
    }

}
