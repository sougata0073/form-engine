package com.sougata.form_engine.dto.formResponse.question;

import com.sougata.form_engine.dto.question.details.MultipleChoiceDetailsDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class MultipleChoiceResponseQuestionDto extends ResponseQuestionDto<MultipleChoiceResponseQuestionDto.Response> {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response extends ResponseByQuestionResponse {

        @JsonSerialize(using = ToStringSerializer.class)
        private Long optionId;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Summary extends ResponseByQuestionSummary {
        private List<MultipleChoiceDetailsDto.Option> options;
    }

}
