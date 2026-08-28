package com.sougata.form_engine.dto.formResponse.question;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sougata.form_engine.constant.QuestionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "questionType",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CheckboxResponseQuestionDto.Summary.class, name = "CHECKBOX"),
        @JsonSubTypes.Type(value = DateResponseQuestionDto.Summary.class, name = "DATE"),
        @JsonSubTypes.Type(value = DateTimeResponseQuestionDto.Summary.class, name = "DATE_TIME"),
        @JsonSubTypes.Type(value = DropdownResponseQuestionDto.Summary.class, name = "DROPDOWN"),
        @JsonSubTypes.Type(value = DurationResponseQuestionDto.Summary.class, name = "DURATION"),
        @JsonSubTypes.Type(value = FileUploadResponseQuestionDto.Summary.class, name = "FILE_UPLOAD"),
        @JsonSubTypes.Type(value = LinearScaleResponseQuestionDto.Summary.class, name = "LINEAR_SCALE"),
        @JsonSubTypes.Type(value = MultipleChoiceResponseQuestionDto.Summary.class, name = "MULTIPLE_CHOICE"),
        @JsonSubTypes.Type(value = MultipleChoiceGridResponseQuestionDto.Summary.class, name = "MULTIPLE_CHOICE_GRID"),
        @JsonSubTypes.Type(value = ParagraphResponseQuestionDto.Summary.class, name = "PARAGRAPH"),
        @JsonSubTypes.Type(value = RatingResponseQuestionDto.Summary.class, name = "RATING"),
        @JsonSubTypes.Type(value = ShortAnswerResponseQuestionDto.Summary.class, name = "SHORT_ANSWER"),
        @JsonSubTypes.Type(value = TickBoxGridResponseQuestionDto.Summary.class, name = "TICK_BOX_GRID"),
        @JsonSubTypes.Type(value = TimeResponseQuestionDto.Summary.class, name = "TIME")
})
@Getter
@Setter
@NoArgsConstructor
public class ResponseByQuestionSummary {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long questionId;

    private String question;

    private QuestionType questionType;

    @JsonProperty(value = "@class")
    private String cls = getClass().getName();
}
