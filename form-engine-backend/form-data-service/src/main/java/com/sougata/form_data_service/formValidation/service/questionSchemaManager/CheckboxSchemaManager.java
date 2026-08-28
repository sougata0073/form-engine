package com.sougata.form_data_service.formValidation.service.questionSchemaManager;

import com.sougata.form_data_service.constant.QuestionType;
import com.sougata.form_data_service.dto.question.request.CheckboxResponsePutReqDto;
import com.sougata.form_data_service.dto.question.response.CheckboxDetailsDto;
import com.sougata.form_data_service.formValidation.exception.ResponseValidationException;
import com.sougata.form_data_service.formValidation.responseValidator.ResponseValidatorFactory;
import com.sougata.form_data_service.formValidation.service.QuestionSchemaManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;

@Service("CHECKBOX_QUESTION_SCHEMA_MANAGER")
public class CheckboxSchemaManager extends QuestionSchemaManager<CheckboxDetailsDto, CheckboxResponsePutReqDto> {

    private final ResponseValidatorFactory responseValidatorFactory;

    @Autowired
    public CheckboxSchemaManager(ResponseValidatorFactory responseValidatorFactory) {
        this.responseValidatorFactory = responseValidatorFactory;
    }

    @Override
    public boolean validateResponse(CheckboxResponsePutReqDto validationDto, CheckboxDetailsDto cb) {
        var optionIdSet = new HashSet<>(cb.getOptions().stream().map(CheckboxDetailsDto.CheckboxOptionResDto::id).toList());
        var invalidResponseOptionIds = new ArrayList<Long>();

        validationDto.getResponseOptionIds().forEach(id -> {
            if (!optionIdSet.contains(id)) {
                invalidResponseOptionIds.add(id);
            }
        });

        if (!invalidResponseOptionIds.isEmpty()) {
            throw new ResponseValidationException(
                    "The following option IDs are not valid for this question: " + invalidResponseOptionIds
            );
        }

        var validator = responseValidatorFactory.getValidator(cb.getValidationConfig().getValidationId());
        var validationConfig = cb.getValidationConfig();
        return validator.isValid(validationDto, validationConfig);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.CHECKBOX;
    }


}
