package com.sougata.form_engine.dto.messaging;

import com.sougata.form_engine.dto.question.responseputrequest.QuestionResponsePutReqDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FormResponseSavedMessage {
    private UUID formId;
    private UUID responderId;
    private List<@Valid QuestionResponsePutReqDto> responses;
}
