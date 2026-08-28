package com.sougata.form_data_service.formValidation.service.questionSchemaManager;

import com.sougata.form_data_service.constant.QuestionType;
import com.sougata.form_data_service.constant.ValidationMessages;
import com.sougata.form_data_service.dto.question.request.RatingResponsePutReqDto;
import com.sougata.form_data_service.dto.question.response.RatingDetailsDto;
import com.sougata.form_data_service.formValidation.exception.ResponseValidationException;
import com.sougata.form_data_service.formValidation.service.QuestionSchemaManager;
import org.springframework.stereotype.Service;

@Service("RATING_QUESTION_SCHEMA_MANAGER")
public class RatingSchemaManager extends QuestionSchemaManager<RatingDetailsDto, RatingResponsePutReqDto> {

    @Override
    public boolean validateResponse(RatingResponsePutReqDto validationDto, RatingDetailsDto rt) {
        var maxRatingNumber = rt.getMaxRatingNumber();

        if (validationDto.getRating() > maxRatingNumber) {
            throw new ResponseValidationException(String.format(ValidationMessages.INVALID_RATING_NUMBER, maxRatingNumber));
        }

        return true;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.RATING;
    }

}
