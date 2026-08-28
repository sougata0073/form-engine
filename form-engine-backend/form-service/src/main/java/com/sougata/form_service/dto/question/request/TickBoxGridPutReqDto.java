package com.sougata.form_service.dto.question.request;

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
public class TickBoxGridPutReqDto extends QuestionPutReqDto {

    private Boolean eachRowRequired;

    @NotNull
    @Size(min = 1, max = 20)
    private List<@Valid Row> rows;

    @NotNull
    @Size(min = 1, max = 20)
    private List<@Valid Column> columns;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Row {
        private Long id;
        private @Length(min = 1) String row;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Column {
        private Long id;
        private @Length(min = 1) String column;
    }

}
