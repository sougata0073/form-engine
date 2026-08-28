package com.sougata.form_service.dto.template.questionTemplate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LinearScaleTemplateDetails extends QuestionTemplateDetails {
    private Integer fromNumber;
    private Integer toNumber;
}
