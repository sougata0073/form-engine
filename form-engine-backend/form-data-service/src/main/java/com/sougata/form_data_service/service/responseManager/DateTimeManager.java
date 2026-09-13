package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.DateTime;
import com.sougata.form_data_service.model.FormResponse;
import com.sougata.form_data_service.repository.DateTimeRepository;
import com.sougata.form_data_service.repository.QuestionResponseRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.responseputrequest.DateTimeResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("DATE_TIME_RESPONSE_MANAGER")
public class DateTimeManager extends ResponseManager<DateTimeResponsePutReqDto> {

    private final DateTimeRepository dateTimeRepository;

    @Autowired
    public DateTimeManager(DateTimeRepository dateTimeRepository, QuestionResponseRepository questionResponseRepository) {
        super(questionResponseRepository);
        this.dateTimeRepository = dateTimeRepository;
    }

    @Override
    public void create(DateTimeResponsePutReqDto response, FormResponse formResponse) {
        DateTime dateTime = new DateTime();

        var qr = createQuestionResponse(response.getQuestionId(), formResponse);

        dateTime.setKey(new AnyTypeQuestionResponse.PartitionKey(response.getQuestionId(), qr.getKey().getQuestionResponseId()));
        dateTime.setDateTime(response.getDateTime());

        dateTimeRepository.save(dateTime);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.DATE_TIME;
    }

    @Override
    public void deleteResponsesByQuestionId(Long questionId) {
        dateTimeRepository.deleteAllByQuestionId(questionId);
    }

    @Override
    public void deleteResponsesByQuestionIdAndQuestionResponseId(Long questionId, Long questionResponseId) {
        dateTimeRepository.deleteAllByQuestionIdAndQuestionResponseId(questionId, questionResponseId);
    }

}
