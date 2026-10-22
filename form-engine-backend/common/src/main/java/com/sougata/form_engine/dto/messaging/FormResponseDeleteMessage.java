package com.sougata.form_engine.dto.messaging;

import com.sougata.form_engine.dto.formResponse.individual.QuestionResponseIndividualDto;
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
public class FormResponseDeleteMessage {
    private UUID formId;
    private UUID formResponseId;
    private UUID userId;
    private List<QuestionResponseIndividualDto> responses;
}
