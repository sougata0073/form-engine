package com.sougata.form_engine.dto.question.details;

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
public class TickBoxGridDetailsDto extends QuestionDetailsDto {
    private Boolean eachRowRequired;
    private List<Row> rows;
    private List<Column> columns;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Row {
        @JsonSerialize(using = ToStringSerializer.class)
        private Long id;
        private String row;
        private Integer orderIndex;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Column {
        @JsonSerialize(using = ToStringSerializer.class)
        private Long id;
        private String column;
        private Integer orderIndex;
    }
}
