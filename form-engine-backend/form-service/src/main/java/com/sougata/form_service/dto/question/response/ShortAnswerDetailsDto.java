package com.sougata.form_service.dto.question.response;

import com.sougata.form_service.validation.configuration.ValidationConfig;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ShortAnswerDetailsDto extends QuestionDetails {
    private ValidationConfig validationConfig;
}
