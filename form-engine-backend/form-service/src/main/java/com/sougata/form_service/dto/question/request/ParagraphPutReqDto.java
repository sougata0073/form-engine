package com.sougata.form_service.dto.question.request;

import com.sougata.form_service.validation.configuration.ValidationConfig;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ParagraphPutReqDto extends QuestionPutReqDto {

    @Valid
    @NotNull
    private ValidationConfig validationConfig;

}
