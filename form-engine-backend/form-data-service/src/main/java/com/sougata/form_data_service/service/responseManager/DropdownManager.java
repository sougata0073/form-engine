package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.Dropdown;
import com.sougata.form_data_service.model.FormResponse;
import com.sougata.form_data_service.repository.DropdownRepository;
import com.sougata.form_data_service.repository.QuestionResponseRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.responseRequest.DropdownResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service("DROPDOWN_RESPONSE_MANAGER")
public class DropdownManager extends ResponseManager<DropdownResponsePutReqDto> {

    private final DropdownRepository dropdownRepository;

    @Autowired
    public DropdownManager(DropdownRepository dropdownRepository, QuestionResponseRepository questionResponseRepository) {
        super(questionResponseRepository);
        this.dropdownRepository = dropdownRepository;
    }

    @Override
    @Transactional
    public void create(DropdownResponsePutReqDto response, FormResponse formResponse) {
        Dropdown dropdown = new Dropdown();

        var qr = createQuestionResponse(response.getQuestionId(), formResponse);

        dropdown.setResponseOptionId(response.getResponseOptionId());
        dropdown.setQuestionResponse(qr);

        dropdownRepository.save(dropdown);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.DROPDOWN;
    }

    @Override
    public void deleteResponsesByQuestion(UUID formId, Long questionId) {
        dropdownRepository.deleteAllByFormIdAndQuestionId(formId, questionId);
    }

    @Override
    public void deleteResponsesByFormResponse(UUID formId, Long formResponseId) {
        dropdownRepository.deleteAllByFormIdAndFormResponseId(formId, formResponseId);
    }
}
