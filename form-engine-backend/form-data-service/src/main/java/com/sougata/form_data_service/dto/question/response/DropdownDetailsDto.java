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
public class DropdownDetailsDto extends QuestionDetailsDto {
    private List<DropdownOptionResDto> options;

    public record DropdownOptionResDto(
            @JsonSerialize(using = ToStringSerializer.class)
            Long id,
            String option,
            Integer orderIndex
    ) {
    }
}
