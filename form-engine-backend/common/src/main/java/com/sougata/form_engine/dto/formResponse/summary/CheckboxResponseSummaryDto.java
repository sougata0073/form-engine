package com.sougata.form_engine.dto.formResponse.summary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

@AllArgsConstructor
@Getter
@Setter
public class CheckboxResponseSummaryDto extends ResponseSummaryDto<CheckboxResponseSummaryDto.Response> {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        @JsonSerialize(using = ToStringSerializer.class)
        private Long optionId;

        private String option;

        @JsonSerialize(using = ToStringSerializer.class)
        private Long responseCount;
    }
}
