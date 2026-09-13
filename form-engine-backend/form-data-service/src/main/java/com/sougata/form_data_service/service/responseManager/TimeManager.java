package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.FormResponse;
import com.sougata.form_data_service.model.Time;
import com.sougata.form_data_service.repository.QuestionResponseRepository;
import com.sougata.form_data_service.repository.TimeRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.responseputrequest.TimeResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        time.setKey(new AnyTypeQuestionResponse.PartitionKey(response.getQuestionId(), qr.getKey().getQuestionResponseId()));
        time.setTime(response.getTime());

        timeRepository.save(time);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.TIME;
    }

    @Override
    public void deleteResponsesByQuestionId(Long questionId) {
        timeRepository.deleteAllByQuestionId(questionId);
    }

    @Override
    public void deleteResponsesByQuestionIdAndQuestionResponseId(Long questionId, Long questionResponseId) {
        timeRepository.deleteAllByQuestionIdAndQuestionResponseId(questionId, questionResponseId);
    }

}
