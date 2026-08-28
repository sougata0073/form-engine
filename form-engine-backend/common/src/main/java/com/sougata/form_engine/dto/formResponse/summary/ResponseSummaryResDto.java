package com.sougata.form_engine.dto.formResponse.summary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseSummaryResDto {
    private List<ResponseSummaryDto<?>> responses;
}
