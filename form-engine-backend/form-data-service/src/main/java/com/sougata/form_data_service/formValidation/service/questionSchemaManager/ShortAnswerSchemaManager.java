package com.sougata.form_data_service.formValidation.service.questionSchemaManager;

import com.sougata.form_data_service.formValidation.responseValidator.ResponseValidatorFactory;
import com.sougata.form_data_service.formValidation.service.QuestionSchemaManager;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.details.ShortAnswerDetailsDto;
import com.sougata.form_engine.dto.question.responseputrequest.ShortAnswerResponsePutReqDto;
import org.springframework.stereotype.Service;

@Service("SHORT_ANSWER_QUESTION_SCHEMA_MANAGER")
public class ShortAnswerSchemaManager extends QuestionSchemaManager<ShortAnswerDetailsDto, ShortAnswerResponsePutReqDto> {

    private final ResponseValidatorFactory responseValidatorFactory;

    public ShortAnswerSchemaManager(ResponseValidatorFactory responseValidatorFactory) {
        this.responseValidatorFactory = responseValidatorFactory;
    }


    @Override
    public boolean validateResponse(ShortAnswerResponsePutReqDto validationDto, ShortAnswerDetailsDto sa) {
        var vConfig = sa.getValidationConfig();

        var validator = responseValidatorFactory.getValidator(vConfig.getValidationId());
        return validator.isValid(validationDto, vConfig);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.SHORT_ANSWER;
    }

}
