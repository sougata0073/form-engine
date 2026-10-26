package com.sougata.form_engine.dto.form;

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
public class FormPresentationSettingsUpdateReqDto extends PatchReqDto {
    private Boolean showProgressBar;
    private Boolean shuffleQuestionOrder;
    private String responseSavedConfirmationMessage;
    private Boolean showLinkToSubmitAnotherResponse;
    private Boolean shareResponseSummaryWithResponders;
    private Boolean disableAutoSaveForAllRespondents;
}
