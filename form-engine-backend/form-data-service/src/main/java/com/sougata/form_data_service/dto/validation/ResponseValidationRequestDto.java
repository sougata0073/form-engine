package com.sougata.form_data_service.dto.validation;

import com.sougata.form_data_service.dto.question.request.QuestionResponsePutReqDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseValidationRequestDto {
    private List<QuestionResponsePutReqDto> responses;
}
