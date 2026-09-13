package com.sougata.form_engine.dto.question.schemaupdatereq;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@NoArgsConstructor
@Getter
@Setter
@FieldNameConstants
public class LinearScaleUpdateReqDto extends QuestionUpdateReqDto {

    private Integer fromNumber;

    private Integer toNumber;

}
