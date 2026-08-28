package com.sougata.form_engine.dto.formResponse.question;

import com.sougata.form_engine.dto.question.details.TickBoxGridDetailsDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class TickBoxGridResponseQuestionDto extends ResponseQuestionDto<TickBoxGridResponseQuestionDto.Response> {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long rowId;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response extends ResponseByQuestionResponse {

        private List<String> columnIds;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Summary extends ResponseByQuestionSummary {
        private List<TickBoxGridDetailsDto.Row> rows;
        private List<TickBoxGridDetailsDto.Column> columns;
    }

}
