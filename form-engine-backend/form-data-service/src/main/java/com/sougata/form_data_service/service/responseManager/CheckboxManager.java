package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.constant.QuestionType;
import com.sougata.form_data_service.dto.question.request.CheckboxResponsePutReqDto;
import com.sougata.form_data_service.model.Checkbox;
import com.sougata.form_data_service.model.CheckboxOption;
import com.sougata.form_data_service.model.FormResponse;
import com.sougata.form_data_service.repository.CheckboxRepository;
import com.sougata.form_data_service.repository.QuestionResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("CHECKBOX_RESPONSE_MANAGER")
public class CheckboxManager extends ResponseManager<
        CheckboxResponsePutReqDto
        > {

    private final CheckboxRepository checkboxRepository;

    @Autowired
    public CheckboxManager(CheckboxRepository checkboxRepository, QuestionResponseRepository questionResponseRepository) {
        super(questionResponseRepository);
        this.checkboxRepository = checkboxRepository;
    }

    @Override
    @Transactional
    public void create(CheckboxResponsePutReqDto response, FormResponse formResponse) {
        Checkbox cb = new Checkbox();

        var qr = createQuestionResponse(response.getQuestionId(), formResponse);

        var responses = response.getResponseOptionIds().stream().map(id -> {
            var op = new CheckboxOption();

            op.setResponseOptionId(id);
            op.setCheckbox(cb);

            return op;
        }).collect(Collectors.toCollection(ArrayList::new));

        cb.setResponses(responses);
        cb.setQuestionResponse(qr);

        checkboxRepository.save(cb);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.CHECKBOX;
    }

    @Override
    public void deleteResponsesByQuestion(UUID formId, Long questionId) {
        checkboxRepository.deleteAllByFormIdAndQuestionId(formId, questionId);
    }

    @Override
    public void deleteResponsesByFormResponse(UUID formId, Long formResponseId) {
        checkboxRepository.deleteAllByFormIdAndFormResponseId(formId, formResponseId);
    }
}
