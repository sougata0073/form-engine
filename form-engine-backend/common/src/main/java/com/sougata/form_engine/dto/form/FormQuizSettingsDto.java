package com.sougata.form_engine.dto.form;

import com.sougata.form_engine.constant.FormQuizReleaseMarksOption;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FormQuizSettingsDto {
    private Boolean isQuiz;
    private FormQuizReleaseMarksOption releaseMarksOption;
    private Boolean respondersCanSeeIncorrectAnswers;
    private Boolean respondersCanSeeCorrectAnswers;
    private Boolean respondersCanSeePointsReceived;
    private Integer questionPointValue;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FormQuizSettingsDto that = (FormQuizSettingsDto) o;
        return Objects.equals(isQuiz, that.isQuiz)
                && releaseMarksOption == that.releaseMarksOption
                && Objects.equals(respondersCanSeeIncorrectAnswers, that.respondersCanSeeIncorrectAnswers)
                && Objects.equals(respondersCanSeeCorrectAnswers, that.respondersCanSeeCorrectAnswers)
                && Objects.equals(respondersCanSeePointsReceived, that.respondersCanSeePointsReceived)
                && Objects.equals(questionPointValue, that.questionPointValue);
    }
}
