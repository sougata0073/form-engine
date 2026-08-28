package com.sougata.form_engine.dto.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormSummaryDto {
    private UUID id;
    private String name;
    private Instant lastOpenedOn;
}
