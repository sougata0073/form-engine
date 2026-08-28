package com.sougata.form_engine.dto.question.details;

import com.sougata.form_engine.dto.validation.config.ValidationConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class CheckboxDetailsDto extends QuestionDetailsDto {
    private List<Option> options;
    private ValidationConfig validationConfig;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Option {
        @JsonSerialize(using = ToStringSerializer.class)
        private Long id;
        private String option;
        private Integer orderIndex;
    }
}
