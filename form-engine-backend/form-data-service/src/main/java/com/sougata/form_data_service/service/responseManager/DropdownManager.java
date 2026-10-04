package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.Dropdown;
import com.sougata.form_data_service.repository.DropdownRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.responseputrequest.DropdownResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service("DROPDOWN_RESPONSE_MANAGER")
public class DropdownManager extends ResponseManager<DropdownResponsePutReqDto> {

    private final DropdownRepository dropdownRepository;

    @Autowired
    public DropdownManager(DropdownRepository dropdownRepository) {
        this.dropdownRepository = dropdownRepository;
    }

    @Override
    @Async
    public CompletableFuture<Void> create(DropdownResponsePutReqDto response, UUID formResponseId) {
        Dropdown dropdown = new Dropdown();

        var key = new AnyTypeQuestionResponse.PartitionKey();
        key.setQuestionId(response.getQuestionId());
        key.setFormResponseId(formResponseId);

        dropdown.setKey(key);
        dropdown.setResponseOptionId(response.getResponseOptionId());

        dropdownRepository.save(dropdown);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteAllByQuestionIdsAndFormResponseId(List<Long> questionIds, UUID formResponseId) {
        dropdownRepository.deleteAllByQuestionIdsAndFormResponseId(questionIds, formResponseId);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.DROPDOWN;
    }

}
