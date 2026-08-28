package com.sougata.form_engine.dto.formResponse.summary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class DurationResponseSummaryDto extends ResponseSummaryDto<DurationResponseSummaryDto.Response> {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer hours;
        private List<DurationCountPair> durations;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DurationCountPair {
        private Integer minutes;

        private Integer seconds;

        @JsonSerialize(using = ToStringSerializer.class)
        private Long count;
    }
}
