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
public class DateResponseSummaryDto extends ResponseSummaryDto<DateResponseSummaryDto.Response> {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer year;
        private Integer month;
        private List<DateCountPair> dates;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DateCountPair {
        private Instant date;

        @JsonSerialize(using = ToStringSerializer.class)
        private Long count;
    }

}
