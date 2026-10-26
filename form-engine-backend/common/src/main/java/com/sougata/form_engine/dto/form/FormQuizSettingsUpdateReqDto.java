package com.sougata.form_engine.dto.form;

import com.sougata.form_engine.constant.FormQuizReleaseMarksOption;
import com.sougata.form_engine.dto.common.PatchReqDto;
import jakarta.validation.constraints.Min;
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
public class FormQuizSettingsUpdateReqDto extends PatchReqDto {
    private Boolean isQuiz;
    private FormQuizReleaseMarksOption releaseMarksOption;
    private Boolean respondersCanSeeIncorrectAnswers;
    private Boolean respondersCanSeeCorrectAnswers;
    private Boolean respondersCanSeePointsReceived;
    private @Min(0) Integer questionPointValue;
}
