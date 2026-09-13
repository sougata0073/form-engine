package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.Checkbox;
import com.sougata.form_data_service.model.FormResponse;
import com.sougata.form_data_service.repository.CheckboxRepository;
import com.sougata.form_data_service.repository.QuestionResponseRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.responseputrequest.CheckboxResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("CHECKBOX_RESPONSE_MANAGER")
public class CheckboxManager extends ResponseManager<CheckboxResponsePutReqDto> {

    private final CheckboxRepository checkboxRepository;

    @Autowired
    public CheckboxManager(CheckboxRepository checkboxRepository, QuestionResponseRepository questionResponseRepository) {
        super(questionResponseRepository);
        this.checkboxRepository = checkboxRepository;
    }

    @Override
    public void create(CheckboxResponsePutReqDto response, FormResponse formResponse) {
        Checkbox cb = new Checkbox();

        var qr = createQuestionResponse(response.getQuestionId(), formResponse);

        cb.setKey(new AnyTypeQuestionResponse.PartitionKey(response.getQuestionId(), qr.getKey().getQuestionResponseId()));
        cb.setResponseOptionIds(response.getResponseOptionIds());

        checkboxRepository.save(cb);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.CHECKBOX;
    }

    @Override
    public void deleteResponsesByQuestionId(Long questionId) {
        checkboxRepository.deleteAllByQuestionId(questionId);
    }

    @Override
    public void deleteResponsesByQuestionIdAndQuestionResponseId(Long questionId, Long questionResponseId) {
        checkboxRepository.deleteAllByQuestionIdAndQuestionResponseId(questionId, questionResponseId);
    }

}
