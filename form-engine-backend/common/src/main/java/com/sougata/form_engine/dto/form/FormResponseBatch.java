package com.sougata.form_engine.dto.form;

import com.sougata.form_engine.dto.question.responseputreqbatch.QuestionResponseBatch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FormResponseBatch {

    private List<RequestPerForm> requests;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class RequestPerForm {
        private UUID formId;
        private List<? extends QuestionResponseBatch<? extends QuestionResponseBatch.Response>> responses;
    }

}
