package com.sougata.form_engine.dto.question.schemaputrequest;

import com.sougata.form_engine.constant.RatingIcon;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RatingPutReqDto extends QuestionPutReqDto {

    @NotNull
    @Min(value = 1)
    @Max(value = 10)
    private Integer maxRatingNumber;

    @NotNull
    private RatingIcon ratingIcon;
}
