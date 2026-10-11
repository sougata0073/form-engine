package com.sougata.form_engine.dto.form;

import com.sougata.form_engine.dto.question.details.QuestionDetailsDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormDetailsDto {
    private UUID id;
    private String name;
    private String title;
    private String description;
    private Boolean published;
    private Boolean acceptingResponse;
    private String notAcceptingResponseMessage;
    private Instant stopAcceptingResponseOn;
    private Integer stopAcceptingResponseAfterResponse;
    private Instant lastOpenedOn;
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

        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(title, that.title)
                && Objects.equals(description, that.description)
                && Objects.equals(published, that.published)
                && Objects.equals(acceptingResponse, that.acceptingResponse)
                && Objects.equals(notAcceptingResponseMessage, that.notAcceptingResponseMessage)
                && Objects.equals(stopAcceptingResponseOn, that.stopAcceptingResponseOn)
                && Objects.equals(stopAcceptingResponseAfterResponse, that.stopAcceptingResponseAfterResponse);
    }
}
