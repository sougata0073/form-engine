package com.sougata.form_engine.dto.question.details;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
public class TickBoxGridDetailsDto extends QuestionDetailsDto {
    private Boolean eachRowRequired;
    private List<Row> rows;
    private List<Column> columns;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TickBoxGridDetailsDto that = (TickBoxGridDetailsDto) o;

        var thisRowsMapById = rows.stream().collect(Collectors.toMap(TickBoxGridDetailsDto.Row::getId, Function.identity()));
        var thatRowsMapById = that.rows.stream().collect(Collectors.toMap(TickBoxGridDetailsDto.Row::getId, Function.identity()));

        for (var entry : thisRowsMapById.entrySet()) {
            if (!Objects.equals(entry.getValue(), thatRowsMapById.get(entry.getKey()))) {
                return false;
            }
        }

        var thisColumnsMapById = columns.stream().collect(Collectors.toMap(TickBoxGridDetailsDto.Column::getId, Function.identity()));
        var thatColumnsMapById = that.columns.stream().collect(Collectors.toMap(TickBoxGridDetailsDto.Column::getId, Function.identity()));

        for (var entry : thisColumnsMapById.entrySet()) {
            if (!Objects.equals(entry.getValue(), thatColumnsMapById.get(entry.getKey()))) {
                return false;
            }
        }

        return super.equals(that) && Objects.equals(eachRowRequired, that.eachRowRequired);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eachRowRequired, rows, columns);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Row {
        @JsonSerialize(using = ToStringSerializer.class)
        private Long id;
        private String row;
        private Integer orderIndex;

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Row row1 = (Row) o;
            return Objects.equals(id, row1.id) && Objects.equals(row, row1.row) && Objects.equals(orderIndex, row1.orderIndex);
        }
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

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Column column1 = (Column) o;
            return Objects.equals(id, column1.id) && Objects.equals(column, column1.column) && Objects.equals(orderIndex, column1.orderIndex);
        }
    }
}
