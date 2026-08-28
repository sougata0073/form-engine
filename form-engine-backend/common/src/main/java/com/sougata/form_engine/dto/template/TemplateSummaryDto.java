package com.sougata.form_engine.dto.template;

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
public class TemplateSummaryDto {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String name;
    private String categoryName;
}
