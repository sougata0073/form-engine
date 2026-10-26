package com.sougata.form_engine.dto.form;

import com.sougata.form_engine.constant.FormEmailCollectionOption;
import com.sougata.form_engine.constant.ResponseCopySendOption;
import com.sougata.form_engine.dto.common.PatchReqDto;
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
public class FormResponseSettingsUpdateReqDto extends PatchReqDto {
    private FormEmailCollectionOption emailCollectionOption;
    private ResponseCopySendOption responseCopySendOption;
    private Boolean isResponseEditable;
    private Boolean singleResponseOnly;
}
