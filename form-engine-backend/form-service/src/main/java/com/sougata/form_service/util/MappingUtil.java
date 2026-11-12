package com.sougata.form_service.util;

import com.sougata.form_engine.dto.form.*;
import com.sougata.form_service.model.formSchema.Form;

public final class MappingUtil {
    public static FormInfoDto toFormInfo(Form form) {
        var generalSettings = form.getFormGeneralSettings();
        var presentationSettings = form.getFormPresentationSettings();
        var quizSettings = form.getFormQuizSettings();
        var responseSettings = form.getFormResponseSettings();

        return new FormInfoDto(
                form.getId(),
                form.getName(),
                form.getTitle(),
                form.getDescription(),
                form.getLastOpenedOn(),
                new FormGeneralSettingsDto(
                        generalSettings.getPublished(),
                        generalSettings.getAcceptingResponse(),
                        generalSettings.getNotAcceptingResponseMessage(),
                        generalSettings.getStopAcceptingResponseOn(),
                        generalSettings.getResponseLimit(),
                        generalSettings.getResponderNeedsToBeAuthenticated()
                ),
                new FormPresentationSettingsDto(
                        presentationSettings.getShowProgressBar(),
                        presentationSettings.getShuffleQuestionOrder(),
                        presentationSettings.getResponseSavedConfirmationMessage(),
                        presentationSettings.getShowLinkToSubmitAnotherResponse(),
                        presentationSettings.getShareResponseSummaryWithResponders(),
                        presentationSettings.getDisableAutoSaveForAllRespondents()
                ),
                new FormQuizSettingsDto(
                        quizSettings.getIsQuiz(),
                        quizSettings.getReleaseMarksOption(),
                        quizSettings.getRespondersCanSeeIncorrectAnswers(),
                        quizSettings.getRespondersCanSeeCorrectAnswers(),
                        quizSettings.getRespondersCanSeePointsReceived(),
                        quizSettings.getQuestionPointValue()
                ),
                new FormResponseSettingsDto(
                        responseSettings.getEmailCollectionOption(),
                        responseSettings.getResponseCopySendOption(),
                        responseSettings.getIsResponseEditable(),
                        responseSettings.getSingleResponseOnly()
                )
        );
    }
}
