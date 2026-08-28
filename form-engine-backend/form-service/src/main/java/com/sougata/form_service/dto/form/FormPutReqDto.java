package com.sougata.form_service.dto.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormPutReqDto {
    private String title;
    private String description;
    private String name;
    private @NotNull Boolean published;
    private @NotNull Boolean acceptingResponse;
    private String notAcceptingResponseMessage;
    private Instant stopAcceptingResponseOn;
    private @Max(value = 1000) Integer stopAcceptingResponseAfterResponse;
}
