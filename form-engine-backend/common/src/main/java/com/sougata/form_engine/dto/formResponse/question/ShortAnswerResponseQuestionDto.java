package com.sougata.form_engine.dto.formResponse.question;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ShortAnswerResponseQuestionDto extends ResponseQuestionDto<ShortAnswerResponseQuestionDto.Response> {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response extends ResponseByQuestionResponse {

        private String text;

    }

    @NoArgsConstructor
    public static class Summary extends ResponseByQuestionSummary {
    }

}
