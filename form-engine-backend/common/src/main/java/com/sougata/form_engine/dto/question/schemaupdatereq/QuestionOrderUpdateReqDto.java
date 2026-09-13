package com.sougata.form_engine.dto.question.schemaupdatereq;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class QuestionOrderUpdateReqDto {
    @NotNull
    Integer currentIndex;
}
