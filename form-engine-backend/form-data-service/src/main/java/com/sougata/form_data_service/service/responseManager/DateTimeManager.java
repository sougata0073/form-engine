package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.constant.QuestionType;
import com.sougata.form_data_service.dto.question.request.DateTimeResponsePutReqDto;
import com.sougata.form_data_service.model.DateTime;
import com.sougata.form_data_service.model.FormResponse;
import com.sougata.form_data_service.repository.DateTimeRepository;
import com.sougata.form_data_service.repository.QuestionResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service("DATE_TIME_RESPONSE_MANAGER")
public class DateTimeManager extends ResponseManager<
        DateTimeResponsePutReqDto
        > {

    private final DateTimeRepository dateTimeRepository;

    @Autowired
    public DateTimeManager(DateTimeRepository dateTimeRepository, QuestionResponseRepository questionResponseRepository) {
        super(questionResponseRepository);
        this.dateTimeRepository = dateTimeRepository;
    }

    @Override
    @Transactional
    public void create(DateTimeResponsePutReqDto response, FormResponse formResponse) {
        DateTime dateTime = new DateTime();

        var qr = createQuestionResponse(response.getQuestionId(), formResponse);

        dateTime.setDateTime(response.getDateTime());
        dateTime.setQuestionResponse(qr);

        dateTimeRepository.save(dateTime);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.DATE_TIME;
    }

    @Override
    public void deleteResponsesByQuestion(UUID formId, Long questionId) {
        dateTimeRepository.deleteAllByFormIdAndQuestionId(formId, questionId);
    }

    @Override
    public void deleteResponsesByFormResponse(UUID formId, Long formResponseId) {
        dateTimeRepository.deleteAllByFormIdAndFormResponseId(formId, formResponseId);
    }
}
