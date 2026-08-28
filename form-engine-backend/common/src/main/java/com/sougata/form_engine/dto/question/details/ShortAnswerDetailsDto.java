package com.sougata.form_engine.dto.question.details;

import com.sougata.form_engine.dto.validation.config.ValidationConfig;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ShortAnswerDetailsDto extends QuestionDetailsDto {
    private ValidationConfig validationConfig;
}
