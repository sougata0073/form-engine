package com.sougata.form_engine.dto.pgfunctionparameter;

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
public class QuestionResponseCounts {

    private List<QuestionResponseCount> counts;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuestionResponseCount {
        private Long questionId;
        private Long responseCount;
        private UUID formId;
    }
}
