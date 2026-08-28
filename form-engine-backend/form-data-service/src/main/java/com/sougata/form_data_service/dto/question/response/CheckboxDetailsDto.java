package com.sougata.form_data_service.dto.question.response;

import com.sougata.form_data_service.dto.validationConfig.ValidationConfig;
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
    private List<CheckboxOptionResDto> options;
    private ValidationConfig validationConfig;

    public record CheckboxOptionResDto(
            @JsonSerialize(using = ToStringSerializer.class)
            Long id,
            String option,
            Integer orderIndex
    ) {
    }

}
