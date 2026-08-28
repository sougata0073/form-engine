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
public class MultipleChoiceGridResponseSummaryDto extends ResponseSummaryDto<MultipleChoiceGridResponseSummaryDto.RowResponse> {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RowResponse {
        @JsonSerialize(using = ToStringSerializer.class)
        private Long rowId;

        private String row;

        private List<ColumnResponse> responses;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ColumnResponse {
        @JsonSerialize(using = ToStringSerializer.class)
        private Long columnId;

        private String column;

        @JsonSerialize(using = ToStringSerializer.class)
        private Long responseCount;
    }
}
