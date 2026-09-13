package com.sougata.form_engine.dto.question.responseputrequest;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CheckboxResponsePutReqDto extends QuestionResponsePutReqDto {

    @NotNull
    @Size(max = 20)
    private Set<@NotNull Long> responseOptionIds;

}
