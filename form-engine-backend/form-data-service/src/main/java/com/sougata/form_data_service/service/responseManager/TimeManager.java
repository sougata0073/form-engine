package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.FormResponse;
import com.sougata.form_data_service.model.Time;
import com.sougata.form_data_service.repository.QuestionResponseRepository;
import com.sougata.form_data_service.repository.TimeRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.responseputrequest.TimeResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service("TIME_RESPONSE_MANAGER")
public class TimeManager extends ResponseManager<TimeResponsePutReqDto> {

    private final TimeRepository timeRepository;

    @Autowired
    public TimeManager(TimeRepository timeRepository, QuestionResponseRepository questionResponseRepository) {
        super(questionResponseRepository);
        this.timeRepository = timeRepository;
    }

    @Override
    @Transactional
    public void create(TimeResponsePutReqDto response, FormResponse formResponse) {
        Time time = new Time();

        var qr = createQuestionResponse(response.getQuestionId(), formResponse);

        time.setTime(response.getTime());
        time.setQuestionResponse(qr);

        timeRepository.save(time);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.TIME;
    }

    @Override
    public void deleteResponsesByQuestion(UUID formId, Long questionId) {
        timeRepository.deleteAllByFormIdAndQuestionId(formId, questionId);
    }

    @Override
    public void deleteResponsesByFormResponse(UUID formId, Long formResponseId) {
        timeRepository.deleteAllByFormIdAndFormResponseId(formId, formResponseId);
    }
}
