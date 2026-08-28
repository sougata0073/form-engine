package com.sougata.form_service.dto.question.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionOrderUpdateReqDto {
    @NotNull
    Integer currentIndex;
}
