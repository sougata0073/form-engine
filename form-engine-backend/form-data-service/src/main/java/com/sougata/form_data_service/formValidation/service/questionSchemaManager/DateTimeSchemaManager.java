package com.sougata.form_data_service.formValidation.service.questionSchemaManager;

import com.sougata.form_data_service.formValidation.service.QuestionSchemaManager;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.details.DateTimeDetailsDto;
import com.sougata.form_engine.dto.question.responseRequest.DateTimeResponsePutReqDto;
import org.springframework.stereotype.Service;

@Service("DATE_TIME_QUESTION_SCHEMA_MANAGER")
public class DateTimeSchemaManager extends QuestionSchemaManager<DateTimeDetailsDto, DateTimeResponsePutReqDto> {

    @Override
    public boolean validateResponse(DateTimeResponsePutReqDto validationDto, DateTimeDetailsDto dt) {
        return true;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.DATE_TIME;
    }

}
