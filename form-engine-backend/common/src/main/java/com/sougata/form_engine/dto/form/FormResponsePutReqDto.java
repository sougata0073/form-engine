package com.sougata.form_engine.dto.form;

import com.sougata.form_engine.dto.question.responseputrequest.QuestionResponsePutReqDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private FormDetailsDto formDetailsResponseSubmittedOn;

    @NotNull
    private List<@NotNull @Valid QuestionResponsePutReqDto> responses;

}
