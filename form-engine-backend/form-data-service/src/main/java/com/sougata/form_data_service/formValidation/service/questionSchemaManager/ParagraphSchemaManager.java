package com.sougata.form_data_service.formValidation.service.questionSchemaManager;

import com.sougata.form_data_service.constant.QuestionType;
import com.sougata.form_data_service.dto.question.request.ParagraphResponsePutReqDto;
import com.sougata.form_data_service.dto.question.response.ParagraphDetailsDto;
import com.sougata.form_data_service.formValidation.responseValidator.ResponseValidatorFactory;
import com.sougata.form_data_service.formValidation.service.QuestionSchemaManager;
import org.springframework.stereotype.Service;

@Service("PARAGRAPH_QUESTION_SCHEMA_MANAGER")
public class ParagraphSchemaManager extends QuestionSchemaManager<ParagraphDetailsDto, ParagraphResponsePutReqDto> {

    private final ResponseValidatorFactory responseValidatorFactory;

    public ParagraphSchemaManager(ResponseValidatorFactory responseValidatorFactory) {
        this.responseValidatorFactory = responseValidatorFactory;
    }

    @Override
    public boolean validateResponse(ParagraphResponsePutReqDto validationDto, ParagraphDetailsDto p) {
        var vConfig = p.getValidationConfig();

        var validator = responseValidatorFactory.getValidator(vConfig.getValidationId());
        return validator.isValid(validationDto, vConfig);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.PARAGRAPH;
    }

}
