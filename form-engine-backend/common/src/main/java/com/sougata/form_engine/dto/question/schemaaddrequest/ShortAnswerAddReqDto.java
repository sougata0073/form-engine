package com.sougata.form_engine.dto.question.schemaaddrequest;

import com.sougata.form_engine.dto.validation.config.ValidationConfig;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ShortAnswerAddReqDto extends QuestionAddReqDto {

    @Valid
    @NotNull
    private ValidationConfig validationConfig;

}
