package com.sougata.form_engine.dto.question.details;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MultipleQuestionDetailsDto {
    private List<QuestionDetailsDto> questions;
}
