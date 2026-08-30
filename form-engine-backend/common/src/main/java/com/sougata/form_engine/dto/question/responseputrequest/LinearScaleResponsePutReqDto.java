package com.sougata.form_engine.dto.question.responseputrequest;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LinearScaleResponsePutReqDto extends QuestionResponsePutReqDto {

    @NotNull
    private Integer scale;

}
