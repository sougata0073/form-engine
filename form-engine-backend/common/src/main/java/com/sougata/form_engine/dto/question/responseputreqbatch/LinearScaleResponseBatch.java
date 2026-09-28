package com.sougata.form_engine.dto.question.responseputreqbatch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LinearScaleResponseBatch extends QuestionResponseBatch<LinearScaleResponseBatch.Response> {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Response extends QuestionResponseBatch.Response {
        private Integer scale;
    }

}
