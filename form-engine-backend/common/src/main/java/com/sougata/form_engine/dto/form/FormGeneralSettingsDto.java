package com.sougata.form_engine.dto.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FormGeneralSettingsDto {
    private Boolean published;
    private Boolean acceptingResponse;
    private String notAcceptingResponseMessage;
    private Instant stopAcceptingResponseOn;
    private Integer responseLimit;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FormGeneralSettingsDto that = (FormGeneralSettingsDto) o;
        return Objects.equals(published, that.published)
                && Objects.equals(acceptingResponse, that.acceptingResponse)
                && Objects.equals(notAcceptingResponseMessage, that.notAcceptingResponseMessage)
                && Objects.equals(stopAcceptingResponseOn, that.stopAcceptingResponseOn)
                && Objects.equals(responseLimit, that.responseLimit);
    }

}
