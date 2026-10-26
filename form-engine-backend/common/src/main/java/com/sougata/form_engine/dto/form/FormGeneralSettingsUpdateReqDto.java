package com.sougata.form_engine.dto.form;

import com.sougata.form_engine.dto.common.PatchReqDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
public class FormGeneralSettingsUpdateReqDto extends PatchReqDto {
    private Boolean published;
    private Boolean acceptingResponse;
    private String notAcceptingResponseMessage;
    private Instant stopAcceptingResponseOn;

    @Min(0)
    @Max(1_00_000)
    private Integer responseLimit;
}
