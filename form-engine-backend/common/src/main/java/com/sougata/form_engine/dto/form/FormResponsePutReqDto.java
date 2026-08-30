package com.sougata.form_engine.dto.form;

import com.sougata.form_engine.dto.question.responseputrequest.QuestionResponsePutReqDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormResponsePutReqDto {
    private List<@Valid QuestionResponsePutReqDto> responses;
}
