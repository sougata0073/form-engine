package com.sougata.form_engine.dto.question.details;

import com.sougata.form_engine.dto.validation.config.ValidationConfig;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
public class ParagraphDetailsDto extends QuestionDetailsDto {
    private ValidationConfig validationConfig;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ParagraphDetailsDto that = (ParagraphDetailsDto) o;
        return super.equals(that) && Objects.equals(validationConfig, that.validationConfig);
    }
}
