package com.sougata.form_engine.dto.question.responseputrequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MultipleChoiceGridResponsePutReqDto extends QuestionResponsePutReqDto {

    @NotNull
    @Size(max = 20)
    private List<@Valid Row> rows;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Row {
        @NotNull
        private Long rowId;

        @NotNull
        private Long responseColumnId;

    }

}
