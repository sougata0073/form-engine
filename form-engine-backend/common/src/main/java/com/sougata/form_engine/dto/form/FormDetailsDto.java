package com.sougata.form_engine.dto.form;

import com.sougata.form_engine.dto.question.details.QuestionDetailsDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormDetailsDto {
    private FormInfoDto formInfo;
    private List<QuestionDetailsDto> questions;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        FormDetailsDto that = (FormDetailsDto) o;

        if (questions.size() != that.questions.size()) {
            return false;
        }

        var thisQuestionsMapById = questions.stream().collect(Collectors.toMap(QuestionDetailsDto::getId, Function.identity()));
        var thatQuestionsMapById = that.questions.stream().collect(Collectors.toMap(QuestionDetailsDto::getId, Function.identity()));

        for (var entry : thisQuestionsMapById.entrySet()) {
            if (!Objects.equals(entry.getValue(), thatQuestionsMapById.get(entry.getKey()))) {
                return false;
            }
        }

        return Objects.equals(formInfo, that.formInfo);
    }
}
