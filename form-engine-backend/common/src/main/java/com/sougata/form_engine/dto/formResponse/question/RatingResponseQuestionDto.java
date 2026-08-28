package com.sougata.form_engine.dto.formResponse.question;

import com.sougata.form_engine.constant.RatingIcon;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RatingResponseQuestionDto extends ResponseQuestionDto<RatingResponseQuestionDto.Response> {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response extends ResponseByQuestionResponse {

        private Integer rating;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Summary extends ResponseByQuestionSummary {
        private RatingIcon ratingIcon;
        private Integer maxRatingNumber;
    }

}
