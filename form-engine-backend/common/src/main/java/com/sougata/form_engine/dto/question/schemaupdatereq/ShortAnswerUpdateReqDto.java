package com.sougata.form_engine.dto.question.schemaupdatereq;

import com.sougata.form_engine.dto.validation.config.ValidationConfig;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@NoArgsConstructor
@Getter
@Setter
@FieldNameConstants
public class ShortAnswerUpdateReqDto extends QuestionUpdateReqDto {

    @Valid
    private ValidationConfig validationConfig;

}
