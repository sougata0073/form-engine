package com.sougata.form_engine.dto.question.schemaaddrequest;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class LinearScaleAddReqDto extends QuestionAddReqDto {

    @NotNull
    private Integer fromNumber;

    @NotNull
    private Integer toNumber;

}
