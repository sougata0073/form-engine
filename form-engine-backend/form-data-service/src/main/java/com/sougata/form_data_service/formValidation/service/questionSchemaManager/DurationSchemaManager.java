package com.sougata.form_data_service.formValidation.service.questionSchemaManager;

import com.sougata.form_data_service.constant.QuestionType;
import com.sougata.form_data_service.dto.question.request.DurationResponsePutReqDto;
import com.sougata.form_data_service.dto.question.response.DurationDetailsDto;
import com.sougata.form_data_service.formValidation.service.QuestionSchemaManager;
import org.springframework.stereotype.Service;

@Service("DURATION_QUESTION_SCHEMA_MANAGER")
public class DurationSchemaManager extends QuestionSchemaManager<DurationDetailsDto, DurationResponsePutReqDto> {

    @Override
    public boolean validateResponse(DurationResponsePutReqDto validationDto, DurationDetailsDto d) {
        return true;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.DURATION;
    }

}
