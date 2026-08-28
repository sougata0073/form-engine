package com.sougata.form_data_service.dto.form;

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
public class FormResponsePutResDto {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
}
