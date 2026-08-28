package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.constant.QuestionType;
import com.sougata.form_data_service.dto.question.request.DurationResponsePutReqDto;
import com.sougata.form_data_service.model.Duration;
import com.sougata.form_data_service.model.FormResponse;
import com.sougata.form_data_service.repository.DurationRepository;
import com.sougata.form_data_service.repository.QuestionResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service("DURATION_RESPONSE_MANAGER")
public class DurationManager extends ResponseManager<
        DurationResponsePutReqDto
        > {

    private final DurationRepository durationRepository;

    @Autowired
    public DurationManager(DurationRepository durationRepository, QuestionResponseRepository questionResponseRepository) {
        super(questionResponseRepository);
        this.durationRepository = durationRepository;
    }

    @Override
    @Transactional
    public void create(DurationResponsePutReqDto response, FormResponse formResponse) {
        Duration duration = new Duration();

        var qr = createQuestionResponse(response.getQuestionId(), formResponse);

        duration.setHours(response.getHours());
        duration.setMinutes(response.getMinutes());
        duration.setSeconds(response.getSeconds());
        duration.setQuestionResponse(qr);

        durationRepository.save(duration);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.DURATION;
    }

    @Override
    public void deleteResponsesByQuestion(UUID formId, Long questionId) {
        durationRepository.deleteAllByFormIdAndQuestionId(formId, questionId);
    }

    @Override
    public void deleteResponsesByFormResponse(UUID formId, Long formResponseId) {
        durationRepository.deleteAllByFormIdAndFormResponseId(formId, formResponseId);
    }
}
