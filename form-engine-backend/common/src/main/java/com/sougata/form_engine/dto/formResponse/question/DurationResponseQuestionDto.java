package com.sougata.form_engine.dto.formResponse.question;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DurationResponseQuestionDto extends ResponseQuestionDto<DurationResponseQuestionDto.Response> {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response extends ResponseByQuestionResponse {

        private Integer hours;
        private Integer minutes;
        private Integer seconds;

    }

    @NoArgsConstructor
    public static class Summary extends ResponseByQuestionSummary {
    }

}
