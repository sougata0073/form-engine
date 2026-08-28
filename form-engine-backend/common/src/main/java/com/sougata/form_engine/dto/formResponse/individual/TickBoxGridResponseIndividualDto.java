package com.sougata.form_engine.dto.formResponse.individual;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TickBoxGridResponseIndividualDto extends ResponseIndividualDto {

    private List<Row> rows;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Row {
        @JsonSerialize(using = ToStringSerializer.class)
        private Long rowId;
        private List<String> columnIds;
    }
}
