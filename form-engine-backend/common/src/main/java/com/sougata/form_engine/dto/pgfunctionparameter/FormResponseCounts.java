package com.sougata.form_engine.dto.pgfunctionparameter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FormResponseCounts {

    private List<FormIdResponseCount> counts;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FormIdResponseCount {
        private UUID formId;
        private Long responseCount;
    }
}
