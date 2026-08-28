package com.sougata.form_engine.dto.formResponse.individual;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MultipleChoiceResponseIndividualDto extends ResponseIndividualDto {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long optionId;
}
