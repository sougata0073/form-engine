package com.sougata.form_engine.dto.question.schemaRequest;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class LinearScalePutReqDto extends QuestionPutReqDto {

    @NotNull
    private Integer fromNumber;

    @NotNull
    private Integer toNumber;

}
