package com.sougata.form_data_service.dto.question.response;

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
