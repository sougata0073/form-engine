package com.sougata.form_engine.dto.formResponse.summary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.time.Instant;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class DateTimeResponseSummaryDto extends ResponseSummaryDto<DateTimeResponseSummaryDto.Response> {

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private LocalDate date;
        private Instant time;

        @JsonSerialize(using = ToStringSerializer.class)
        private Long timeCount;
    }

}
