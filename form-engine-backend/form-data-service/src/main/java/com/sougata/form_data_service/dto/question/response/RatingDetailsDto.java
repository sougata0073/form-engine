package com.sougata.form_data_service.dto.question.response;

import com.sougata.form_data_service.constant.RatingIcon;
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
