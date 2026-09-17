package com.sougata.form_engine.dto.question.schemaupdatereq;

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
public class MultipleQuestionUpdateReqDto {
    @NotNull
    private List<QuestionUpdateReqDto> questions;
}
