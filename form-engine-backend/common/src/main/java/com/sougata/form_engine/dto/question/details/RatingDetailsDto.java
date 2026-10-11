package com.sougata.form_engine.dto.question.details;

import com.sougata.form_engine.constant.RatingIcon;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
public class RatingDetailsDto extends QuestionDetailsDto {
    private Integer maxRatingNumber;
    private RatingIcon ratingIcon;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RatingDetailsDto that = (RatingDetailsDto) o;
        return super.equals(that) && Objects.equals(maxRatingNumber, that.maxRatingNumber) && ratingIcon == that.ratingIcon;
    }
}
