package com.sougata.form_engine.dto.question.details;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class LinearScaleDetailsDto extends QuestionDetailsDto {
    private Integer fromNumber;
    private Integer toNumber;
}
