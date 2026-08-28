package com.sougata.form_service.dto.template.questionTemplate;

import com.sougata.form_service.constant.RatingIcon;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingTemplateDetails extends QuestionTemplateDetails {
    private Integer maxRatingNumber;
    private RatingIcon ratingIcon;
}
