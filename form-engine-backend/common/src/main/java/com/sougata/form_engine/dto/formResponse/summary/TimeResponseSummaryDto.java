package com.sougata.form_engine.dto.formResponse.summary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class TimeResponseSummaryDto extends ResponseSummaryDto<TimeResponseSummaryDto.Response> {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer hour;
        private List<TimeCountPair> times;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TimeCountPair {
        private Instant time;

        @JsonSerialize(using = ToStringSerializer.class)
        private Long count;
    }

}
