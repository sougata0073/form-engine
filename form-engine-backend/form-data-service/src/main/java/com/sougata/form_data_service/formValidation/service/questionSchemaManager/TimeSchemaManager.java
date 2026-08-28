package com.sougata.form_data_service.formValidation.service.questionSchemaManager;

import com.sougata.form_data_service.constant.QuestionType;
import com.sougata.form_data_service.dto.question.request.TimeResponsePutReqDto;
import com.sougata.form_data_service.dto.question.response.TimeDetailsDto;
import com.sougata.form_data_service.formValidation.service.QuestionSchemaManager;
import org.springframework.stereotype.Service;

@Service("TIME_QUESTION_SCHEMA_MANAGER")
public class TimeSchemaManager extends QuestionSchemaManager<TimeDetailsDto, TimeResponsePutReqDto> {

    @Override
    public boolean validateResponse(TimeResponsePutReqDto validationDto, TimeDetailsDto t) {
        return true;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.TIME;
    }

}
