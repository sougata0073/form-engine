package com.sougata.form_engine.dto.question.responseputrequest;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DurationResponsePutReqDto extends QuestionResponsePutReqDto {

    @NotNull
    @Min(value = 0)
    @Max(value = 72)
    private Integer hours;

    @NotNull
    @Min(value = 0)
    @Max(value = 59)
    private Integer minutes;

    @NotNull
    @Min(value = 0)
    @Max(value = 59)
    private Integer seconds;

}
