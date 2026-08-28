package com.sougata.form_data_service.service.impl;

import com.sougata.form_data_service.dto.common.SuccessMessageDto;
import com.sougata.form_data_service.dto.form.FormResponsePutReqDto;
import com.sougata.form_data_service.dto.form.FormResponsePutResDto;
import com.sougata.form_data_service.dto.validation.ResponseValidationRequestDto;
import com.sougata.form_data_service.exception.FormSubmitException;
import com.sougata.form_data_service.feignClient.FormServiceFeignClient;
import com.sougata.form_data_service.formValidation.service.FormSchemaService;
import com.sougata.form_data_service.model.FormResponse;
import com.sougata.form_data_service.repository.FormResponseRepository;
import com.sougata.form_data_service.repository.QuestionResponseRepository;
import com.sougata.form_data_service.service.FormResponseService;
import com.sougata.form_data_service.service.responseManager.ResponseManagerFactory;
import com.sougata.form_engine.constant.MessagingChannelNames;
import com.sougata.form_engine.dto.messaging.FormResponseDeleteMessage;
import com.sougata.form_engine.dto.messaging.FormResponseSavedMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FormResponseServiceImpl implements FormResponseService {

    private final FormResponseRepository formResponseRepository;
    private final ResponseManagerFactory responseManagerFactory;
    private final FormServiceFeignClient formServiceFeignClient;
    private final FormSchemaService formSchemaService;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional
    public FormResponsePutResDto saveResponse(UUID formId, FormResponsePutReqDto req, UUID userId) {

        var formDetails = formServiceFeignClient.getFormDetails(formId);

        if (!formDetails.getPublished()) {
            throw new FormSubmitException("This form is not published yet. Form ID: " + formId);
        }

        boolean isAcceptingDateExceeded =
                formDetails.getStopAcceptingResponseOn() != null &&
                        Instant.now().isAfter(formDetails.getStopAcceptingResponseOn());

        boolean isNumberOfResponseExceeded = formDetails.getStopAcceptingResponseAfterResponse() != null &&
                formResponseRepository.getFormResponseCount(formId) >= Integer.toUnsignedLong(formDetails.getStopAcceptingResponseAfterResponse());

        if (!formDetails.getAcceptingResponse() || isAcceptingDateExceeded || isNumberOfResponseExceeded) {
            throw new FormSubmitException("This form is not accepting response. Form ID: " + formId);
        }

        var validationBody = new ResponseValidationRequestDto(req.getResponses());

        var validationResponse = formSchemaService.validateResponse(formId, formDetails, validationBody);

        var formResponse = new FormResponse();

        formResponse.setFormId(formId);
        formResponse.setUserId(userId);

        var savedFormResponse = formResponseRepository.save(formResponse);

        req.getResponses().forEach(response -> {
            var responseManager = responseManagerFactory.get(
                    response.getQuestionType()
            );
            responseManager.create(response, savedFormResponse);
        });

        redisTemplate.convertAndSend(MessagingChannelNames.FORM_RESPONSE_SAVED, new FormResponseSavedMessage(formId));

        return new FormResponsePutResDto(savedFormResponse.getId());
    }

    @Override
    public SuccessMessageDto deleteFormResponse(UUID formId, UUID userId, Long formResponseId) {
        formResponseRepository.deleteByFormResponseId(formResponseId);

        redisTemplate.convertAndSend(
                MessagingChannelNames.FORM_RESPONSE_DELETED,
                new FormResponseDeleteMessage(formId, formResponseId, userId)
        );

        return SuccessMessageDto.create("Response deleted successfully. Form id: " + formId + " Form response ID: " + formResponseId);
    }
}
