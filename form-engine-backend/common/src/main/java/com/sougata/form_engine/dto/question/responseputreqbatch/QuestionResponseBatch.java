package com.sougata.form_engine.dto.question.responseputreqbatch;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.f4b6a3.tsid.TsidCreator;
import com.sougata.form_engine.constant.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "questionType",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CheckboxResponseBatch.class, name = "CHECKBOX"),
        @JsonSubTypes.Type(value = DateResponseBatch.class, name = "DATE"),
        @JsonSubTypes.Type(value = DateTimeResponseBatch.class, name = "DATE_TIME"),
        @JsonSubTypes.Type(value = DropdownResponseBatch.class, name = "DROPDOWN"),
        @JsonSubTypes.Type(value = DurationResponseBatch.class, name = "DURATION"),
        @JsonSubTypes.Type(value = FileUploadResponseBatch.class, name = "FILE_UPLOAD"),
        @JsonSubTypes.Type(value = LinearScaleResponseBatch.class, name = "LINEAR_SCALE"),
        @JsonSubTypes.Type(value = MultipleChoiceResponseBatch.class, name = "MULTIPLE_CHOICE"),
        @JsonSubTypes.Type(value = MultipleChoiceGridResponseBatch.class, name = "MULTIPLE_CHOICE_GRID"),
        @JsonSubTypes.Type(value = ParagraphResponseBatch.class, name = "PARAGRAPH"),
        @JsonSubTypes.Type(value = RatingResponseBatch.class, name = "RATING"),
        @JsonSubTypes.Type(value = ShortAnswerResponseBatch.class, name = "SHORT_ANSWER"),
        @JsonSubTypes.Type(value = TickBoxGridResponseBatch.class, name = "TICK_BOX_GRID"),
        @JsonSubTypes.Type(value = TimeResponseBatch.class, name = "TIME")
})
@Getter
@Setter
@NoArgsConstructor
public class QuestionResponseBatch<TResponse extends QuestionResponseBatch.Response> {

    private Long questionId;

    private QuestionType questionType;

    private List<TResponse> responses;

    @JsonProperty(value = "@class")
    private String cls = getClass().getName();

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Response {
        private Long responseCount;
        private Long questionResponseId = TsidCreator.getTsid().toLong();
        private List<UUID> formResponseIds;
    }

}
