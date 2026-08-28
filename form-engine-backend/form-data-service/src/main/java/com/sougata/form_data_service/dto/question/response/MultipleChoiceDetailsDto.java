package com.sougata.form_data_service.dto.question.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class MultipleChoiceDetailsDto extends QuestionDetailsDto {
    private List<MultipleChoiceOptionResDto> options;

    public record MultipleChoiceOptionResDto(
            @JsonSerialize(using = ToStringSerializer.class)
            Long id,
            String option,
            Integer orderIndex
    ) {
    }
}
