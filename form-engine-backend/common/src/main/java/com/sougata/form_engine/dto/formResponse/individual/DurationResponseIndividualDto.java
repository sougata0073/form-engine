package com.sougata.form_engine.dto.formResponse.individual;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DurationResponseIndividualDto extends ResponseIndividualDto {
    private Integer hours;
    private Integer minutes;
    private Integer seconds;
}
