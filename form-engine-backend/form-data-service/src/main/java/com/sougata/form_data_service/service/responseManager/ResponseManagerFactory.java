package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.exception.NoResponseManagerFoundException;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.QuestionResponseIndividualDto;
import com.sougata.form_engine.dto.question.responseputrequest.QuestionResponsePutReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ResponseManagerFactory {

    private final ApplicationContext applicationContext;

    @SuppressWarnings("unchecked")
    public <QR extends QuestionResponsePutReqDto, QRID extends QuestionResponseIndividualDto>
    ResponseManager<QR, QRID> get(QuestionType questionType) {
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
    public <QR extends QuestionResponsePutReqDto, QRID extends QuestionResponseIndividualDto>
    List<ResponseManager<QR, QRID>> getAll() {
        List<ResponseManager<QR, QRID>> repos = new ArrayList<>();

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
