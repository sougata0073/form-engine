package com.sougata.form_engine.dto.template;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TemplateSummariesDto {
    private List<TemplateSummaryDto> templates;
}
