package com.sougata.form_data_service.formValidation.service;

import com.sougata.form_data_service.constant.ValidationMessages;
import com.sougata.form_data_service.formValidation.exception.ResponseValidationException;
import com.sougata.form_data_service.formValidation.service.questionSchemaManager.QuestionSchemaManagerFactory;
import com.sougata.form_engine.dto.form.FormDetailsDto;
import com.sougata.form_engine.dto.others.SuccessMessageDto;
import com.sougata.form_engine.dto.question.details.QuestionDetailsDto;
import com.sougata.form_engine.dto.question.responseputrequest.QuestionResponsePutReqDto;
import com.sougata.form_engine.dto.validation.request.ResponseValidationRequestDto;
import com.sougata.form_engine.exception.FormDetailsMayChangedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FormSchemaService {

    private final QuestionSchemaManagerFactory questionSchemaManagerFactory;

    @Autowired
    public FormSchemaService(QuestionSchemaManagerFactory questionSchemaManagerFactory) {
        this.questionSchemaManagerFactory = questionSchemaManagerFactory;
    }

    public SuccessMessageDto validateResponse(UUID formId, FormDetailsDto formDetails, ResponseValidationRequestDto dto) {

        var questionResList = formDetails.getQuestions();

        // Getting IDs of questions which are marked as required
        Set<Long> requiredQuestionIds = questionResList.stream().filter(QuestionDetailsDto::getRequired).map(QuestionDetailsDto::getId).collect(Collectors.toSet());

        // Putting question IDs of all responses into a HashSet
        Set<Long> responseQuestionIds = dto.getResponses().stream().map(QuestionResponsePutReqDto::getQuestionId).collect(Collectors.toSet());

        requiredQuestionIds.forEach(qId -> {
            if (!responseQuestionIds.contains(qId)) {
                throw new FormDetailsMayChangedException();
            }
        });

        var questionDetailsMap = questionResList.stream().collect(Collectors.toMap(QuestionDetailsDto::getId, Function.identity()));

        // Now passing each response into validators
        dto.getResponses().forEach(vReq -> {
            var questionManager = questionSchemaManagerFactory.get(vReq.getQuestionType());
            var questionDetails = questionDetailsMap.get(vReq.getQuestionId());

            if (questionDetails == null) {
                throw new FormDetailsMayChangedException();
            }

            boolean isValid = questionManager.validateResponse(vReq, questionDetailsMap.get(vReq.getQuestionId()));

            if (!isValid) {
                throw new ResponseValidationException(
                        String.format(ValidationMessages.INVALID_RESPONSE, vReq.getQuestionType())
                );
            }
        });

        return SuccessMessageDto.create("All responses are valid");
    }


}
