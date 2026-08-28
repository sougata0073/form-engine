package com.sougata.form_engine.dto.validation.request;

import com.sougata.form_engine.dto.question.responseRequest.QuestionResponsePutReqDto;
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
