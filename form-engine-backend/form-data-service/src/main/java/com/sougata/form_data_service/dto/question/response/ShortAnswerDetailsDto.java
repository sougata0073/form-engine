package com.sougata.form_data_service.dto.question.response;

import com.sougata.form_data_service.dto.validationConfig.ValidationConfig;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ShortAnswerDetailsDto extends QuestionDetailsDto {
    private ValidationConfig validationConfig;
}
