package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.exception.NoResponseManagerFoundException;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.responseputrequest.QuestionResponsePutReqDto;
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
    public <QR extends QuestionResponsePutReqDto> ResponseManager<QR> get(QuestionType questionType) {
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
    public <QR extends QuestionResponsePutReqDto> List<ResponseManager<QR>> getAll() {
        List<ResponseManager<QR>> repos = new ArrayList<>();

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
