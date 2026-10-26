package com.sougata.form_engine.dto.form;

import com.sougata.form_engine.dto.common.PatchReqDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
public class FormUpdateReqDto extends PatchReqDto {
    private String name;
    private String title;
    private String description;
    private @Valid FormDefaultSettingsUpdateReqDto defaultSettingsUpdateReq;
    private @Valid FormGeneralSettingsUpdateReqDto generalSettingsUpdateReq;
    private @Valid FormPresentationSettingsUpdateReqDto presentationSettingsUpdateReq;
    private @Valid FormQuizSettingsUpdateReqDto quizSettingsUpdateReq;
    private @Valid FormResponseSettingsUpdateReqDto responseSettingsUpdateReq;
}
