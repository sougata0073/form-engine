package com.sougata.form_service.dto.question.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class LinearScaleDetailsDto extends QuestionDetails {
    private Integer fromNumber;
    private Integer toNumber;
}
