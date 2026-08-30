package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.Date;
import com.sougata.form_data_service.model.FormResponse;
import com.sougata.form_data_service.repository.DateRepository;
import com.sougata.form_data_service.repository.QuestionResponseRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.responseRequest.DateResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service("DATE_RESPONSE_MANAGER")
public class DateManager extends ResponseManager<
        DateResponsePutReqDto
        > {

    private final DateRepository dateRepository;

    @Autowired
    public DateManager(DateRepository dateRepository, QuestionResponseRepository questionResponseRepository) {
        super(questionResponseRepository);
        this.dateRepository = dateRepository;
    }

    @Override
    @Transactional
    public void create(DateResponsePutReqDto response, FormResponse formResponse) {
        Date date = new Date();
        var qr = createQuestionResponse(response.getQuestionId(), formResponse);

        date.setQuestionResponse(qr);
        date.setDate(response.getDate());

        dateRepository.save(date);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.DATE;
    }

    @Override
    public void deleteResponsesByQuestion(UUID formId, Long questionId) {
        dateRepository.deleteAllByFormIdAndQuestionId(formId, questionId);
    }

    @Override
    public void deleteResponsesByFormResponse(UUID formId, Long formResponseId) {
        dateRepository.deleteAllByFormIdAndFormResponseId(formId, formResponseId);
    }
}
