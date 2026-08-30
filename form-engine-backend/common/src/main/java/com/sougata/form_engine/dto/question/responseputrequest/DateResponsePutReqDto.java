package com.sougata.form_engine.dto.question.responseputrequest;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DateResponsePutReqDto extends QuestionResponsePutReqDto {

    @NotNull
    private Instant date;

}
