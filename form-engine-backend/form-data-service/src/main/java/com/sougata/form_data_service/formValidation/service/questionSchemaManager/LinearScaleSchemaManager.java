package com.sougata.form_data_service.formValidation.service.questionSchemaManager;

import com.sougata.form_data_service.constant.ExceptionMessages;
import com.sougata.form_data_service.formValidation.exception.ResponseValidationException;
import com.sougata.form_data_service.formValidation.service.QuestionSchemaManager;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.details.LinearScaleDetailsDto;
import com.sougata.form_engine.dto.question.responseputrequest.LinearScaleResponsePutReqDto;
import org.springframework.stereotype.Service;

@Service("LINEAR_SCALE_QUESTION_SCHEMA_MANAGER")
public class LinearScaleSchemaManager extends QuestionSchemaManager<LinearScaleDetailsDto, LinearScaleResponsePutReqDto> {

    @Override
    public boolean validateResponse(LinearScaleResponsePutReqDto validationDto, LinearScaleDetailsDto ls) {
        Integer toNumber = ls.getToNumber();

        if (validationDto.getScale() > toNumber) {
            throw new ResponseValidationException(
                    String.format(
                            ExceptionMessages.INVALID_SCALE, toNumber, validationDto.getScale()
                    )
            );
        }

        return true;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.LINEAR_SCALE;
    }

}
