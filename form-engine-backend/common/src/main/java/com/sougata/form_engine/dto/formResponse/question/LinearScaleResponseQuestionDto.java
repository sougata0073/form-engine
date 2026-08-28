package com.sougata.form_engine.dto.formResponse.question;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class LinearScaleResponseQuestionDto extends ResponseQuestionDto<LinearScaleResponseQuestionDto.Response> {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response extends ResponseByQuestionResponse {

        private Integer scale;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Summary extends ResponseByQuestionSummary {
        private Integer fromNumber;
        private Integer toNumber;
    }

}
