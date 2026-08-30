package com.sougata.form_engine.dto.question.schemaRequest;

import com.sougata.form_engine.dto.validation.config.ValidationConfig;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class CheckboxPutReqDto extends QuestionPutReqDto {

    @NotNull
    @Size(min = 1, max = 20)
    private List<@Valid Option> options;

    @Valid
    @NotNull
    private ValidationConfig validationConfig;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Option {
        private Long id;
        private @Length(min = 1) String option;
    }

}