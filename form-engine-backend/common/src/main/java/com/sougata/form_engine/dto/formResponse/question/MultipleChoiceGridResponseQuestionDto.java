package com.sougata.form_engine.dto.formResponse.question;

import com.sougata.form_engine.dto.question.details.MultipleChoiceGridDetailsDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class MultipleChoiceGridResponseQuestionDto extends ResponseQuestionDto<MultipleChoiceGridResponseQuestionDto.Response> {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long rowId;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response extends ResponseByQuestionResponse {
        @JsonSerialize(using = ToStringSerializer.class)
        private Long columnId;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    public static class Summary extends ResponseByQuestionSummary {
        private List<MultipleChoiceGridDetailsDto.Row> rows;
        private List<MultipleChoiceGridDetailsDto.Column> columns;
    }

}
