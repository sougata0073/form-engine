package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.Dropdown;
import com.sougata.form_data_service.model.FormResponse;
import com.sougata.form_data_service.repository.DropdownRepository;
import com.sougata.form_data_service.repository.QuestionResponseRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.responseputrequest.DropdownResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        dropdown.setKey(new AnyTypeQuestionResponse.PartitionKey(response.getQuestionId(), qr.getKey().getQuestionResponseId()));
        dropdown.setResponseOptionId(response.getResponseOptionId());

        dropdownRepository.save(dropdown);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.DROPDOWN;
    }

    @Override
    public void deleteResponsesByQuestionId(Long questionId) {
        dropdownRepository.deleteAllByQuestionId(questionId);
    }

    @Override
    public void deleteResponsesByQuestionIdAndQuestionResponseId(Long questionId, Long questionResponseId) {
        dropdownRepository.deleteAllByQuestionIdAndQuestionResponseId(questionId, questionResponseId);
    }

}
