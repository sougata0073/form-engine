package com.sougata.form_engine.dto.question.schemaupdatereq;

import com.sougata.form_engine.constant.RatingIcon;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@NoArgsConstructor
@Getter
@Setter
@FieldNameConstants
public class RatingUpdateReqDto extends QuestionUpdateReqDto {

    @Min(value = 1)
    @Max(value = 10)
    private Integer maxRatingNumber;

    private RatingIcon ratingIcon;
}
