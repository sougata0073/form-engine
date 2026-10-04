package com.sougata.form_engine.dto.question.responseputreqbatch;

import com.sougata.form_engine.dto.question.responseputrequest.QuestionResponsePutReqDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class FormResponseInfoQuestionResponse<TQuestionResponsePutReq extends QuestionResponsePutReqDto> {
    private UUID formResponseId;
    private UUID userId;
    private TQuestionResponsePutReq questionResponsePutReq;
}
