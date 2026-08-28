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
public class TickBoxGridDetailsDto extends QuestionDetailsDto {
    private Boolean eachRowRequired;
    private List<TickBoxGridRowResDto> rows;
    private List<TickBoxGridColumnResDto> columns;

    public record TickBoxGridRowResDto(
            @JsonSerialize(using = ToStringSerializer.class)
            Long id,
            String row,
            Integer orderIndex
    ) {
    }

    public record TickBoxGridColumnResDto(
            @JsonSerialize(using = ToStringSerializer.class)
            Long id,
            String column,
            Integer orderIndex
    ) {
    }
}
