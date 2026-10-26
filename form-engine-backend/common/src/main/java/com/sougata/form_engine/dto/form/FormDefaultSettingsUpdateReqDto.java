package com.sougata.form_engine.dto.form;

import com.sougata.form_engine.constant.FormEmailCollectionOption;
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
public class FormDefaultSettingsUpdateReqDto extends PatchReqDto {
    private FormEmailCollectionOption defaultEmailCollectionOption;
    private Boolean makeQuestionsRequiredByDefault;
}
