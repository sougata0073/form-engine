package com.sougata.form_engine.dto.question.details;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
public class LinearScaleDetailsDto extends QuestionDetailsDto {
    private Integer fromNumber;
    private Integer toNumber;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LinearScaleDetailsDto that = (LinearScaleDetailsDto) o;
        return super.equals(that) && Objects.equals(fromNumber, that.fromNumber) && Objects.equals(toNumber, that.toNumber);
    }
}
