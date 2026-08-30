package com.sougata.form_data_service.formValidation.service.questionSchemaManager;

import com.sougata.form_data_service.formValidation.exception.ResponseValidationException;
import com.sougata.form_data_service.formValidation.service.QuestionSchemaManager;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.details.MultipleChoiceDetailsDto;
import com.sougata.form_engine.dto.question.responseputrequest.MultipleChoiceResponsePutReqDto;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("MULTIPLE_CHOICE_QUESTION_SCHEMA_MANAGER")
public class MultipleChoiceSchemaManager extends QuestionSchemaManager<MultipleChoiceDetailsDto, MultipleChoiceResponsePutReqDto> {

    @Override
    public boolean validateResponse(MultipleChoiceResponsePutReqDto validationDto, MultipleChoiceDetailsDto mc) {
        var present = mc.getOptions().stream()
                .anyMatch(op -> Objects.equals(op.getId(), validationDto.getResponseOptionId()));

        if (!present) {
            throw new ResponseValidationException(
                    "Invalid dropdown option ID: " + validationDto.getResponseOptionId()
            );
        }

        return true;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.MULTIPLE_CHOICE;
    }


}
