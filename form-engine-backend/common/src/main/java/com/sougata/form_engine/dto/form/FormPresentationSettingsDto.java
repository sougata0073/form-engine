package com.sougata.form_engine.dto.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FormPresentationSettingsDto {
    private Boolean showProgressBar;
    private Boolean shuffleQuestionOrder;
    private String responseSavedConfirmationMessage;
    private Boolean showLinkToSubmitAnotherResponse;
    private Boolean shareResponseSummaryWithResponders;
    private Boolean disableAutoSaveForAllRespondents;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FormPresentationSettingsDto that = (FormPresentationSettingsDto) o;
        return Objects.equals(showProgressBar, that.showProgressBar)
                && Objects.equals(shuffleQuestionOrder, that.shuffleQuestionOrder)
                && Objects.equals(responseSavedConfirmationMessage, that.responseSavedConfirmationMessage)
                && Objects.equals(showLinkToSubmitAnotherResponse, that.showLinkToSubmitAnotherResponse)
                && Objects.equals(shareResponseSummaryWithResponders, that.shareResponseSummaryWithResponders)
                && Objects.equals(disableAutoSaveForAllRespondents, that.disableAutoSaveForAllRespondents);
    }
}
