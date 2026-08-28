package com.sougata.form_engine.dto.question.details;

import com.sougata.form_engine.constant.RatingIcon;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RatingDetailsDto extends QuestionDetailsDto {
    private Integer maxRatingNumber;
    private RatingIcon ratingIcon;
}
