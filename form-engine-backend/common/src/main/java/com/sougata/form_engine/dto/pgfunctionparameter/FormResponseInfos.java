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
public class FormResponseInfos {
    private List<FormResponseInfo> formResponseInfos;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FormResponseInfo {
        private UUID formResponseId;
        private UUID formId;
        private UUID userId;
    }
}
