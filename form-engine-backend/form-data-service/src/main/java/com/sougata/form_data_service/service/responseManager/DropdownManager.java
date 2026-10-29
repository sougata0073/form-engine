package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.Dropdown;
import com.sougata.form_data_service.repository.DropdownRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.DropdownResponseIndividualDto;
import com.sougata.form_engine.dto.question.responseputrequest.DropdownResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service("DROPDOWN_RESPONSE_MANAGER")
public class DropdownManager extends ResponseManager<DropdownResponsePutReqDto, DropdownResponseIndividualDto> {

    private final DropdownRepository dropdownRepository;

    @Autowired
    public DropdownManager(DropdownRepository dropdownRepository) {
        this.dropdownRepository = dropdownRepository;
    }

    @Override
    @Async
    public CompletableFuture<Void> create(UUID formId, UUID formResponseId, DropdownResponsePutReqDto response) {
        Dropdown dropdown = new Dropdown();

        var key = new AnyTypeQuestionResponse.PartitionKey(
                formId,
                formResponseId,
                response.getQuestionId()
        );

        dropdown.setKey(key);
        dropdown.setResponseOptionId(response.getResponseOptionId());

        dropdownRepository.save(dropdown);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<List<DropdownResponseIndividualDto>> getResponses(UUID formId, UUID formResponseId, Set<Long> questionIds) {
        var responses = dropdownRepository.findAllByQuestionIds(formId, formResponseId, questionIds);

        return CompletableFuture.completedFuture(
                responses.stream().map(res -> {
                    var dr = new DropdownResponseIndividualDto();

                    dr.setQuestionId(res.getKey().getQuestionId());
                    dr.setQuestionType(getQuestionType());
                    dr.setOptionId(res.getResponseOptionId());

                    return dr;
                }).toList()
        );
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteAllByQuestionIds(UUID formId, UUID formResponseId, Set<Long> questionIds) {
        dropdownRepository.deleteAllByQuestionIds(formId, formResponseId, questionIds);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteAllByFormId(UUID formId) {
        dropdownRepository.deleteAllByFormId(formId);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.DROPDOWN;
    }

}
