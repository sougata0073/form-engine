package com.sougata.form_engine.dto.question.schemaputrequest;

import com.sougata.form_engine.dto.validation.config.ValidationConfig;
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
