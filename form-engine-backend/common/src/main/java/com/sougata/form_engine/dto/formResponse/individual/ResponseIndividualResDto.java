package com.sougata.form_engine.dto.formResponse.individual;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseIndividualResDto {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long formResponseId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long page;

    private UUID userId;

    private List<ResponseIndividualDto> responses;
}
