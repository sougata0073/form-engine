package com.sougata.form_engine.dto.formResponse.individual;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormResponseIndividualDtoNoPage {

    private UUID formResponseId;

    private UUID userId;

    private List<QuestionResponseIndividualDto> responses;

}
