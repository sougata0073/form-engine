package com.sougata.form_engine.dto.formResponse.summary;

import com.sougata.form_engine.constant.RatingIcon;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RatingResponseSummaryDto extends ResponseSummaryDto<RatingResponseSummaryDto.Response> {

    @JsonSerialize(using = ToStringSerializer.class)
    private Double averageRating;

    private RatingIcon ratingIcon;

    private Integer maxRatingNumber;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer rating;

        @JsonSerialize(using = ToStringSerializer.class)
        private Long responseCount;
    }

}
