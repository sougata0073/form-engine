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

import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "questionType",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CheckboxResponseQuestionDto.class, name = "CHECKBOX"),
        @JsonSubTypes.Type(value = DateResponseQuestionDto.class, name = "DATE"),
        @JsonSubTypes.Type(value = DateTimeResponseQuestionDto.class, name = "DATE_TIME"),
        @JsonSubTypes.Type(value = DropdownResponseQuestionDto.class, name = "DROPDOWN"),
        @JsonSubTypes.Type(value = DurationResponseQuestionDto.class, name = "DURATION"),
        @JsonSubTypes.Type(value = FileUploadResponseQuestionDto.class, name = "FILE_UPLOAD"),
        @JsonSubTypes.Type(value = LinearScaleResponseQuestionDto.class, name = "LINEAR_SCALE"),
        @JsonSubTypes.Type(value = MultipleChoiceResponseQuestionDto.class, name = "MULTIPLE_CHOICE"),
        @JsonSubTypes.Type(value = MultipleChoiceGridResponseQuestionDto.class, name = "MULTIPLE_CHOICE_GRID"),
        @JsonSubTypes.Type(value = ParagraphResponseQuestionDto.class, name = "PARAGRAPH"),
        @JsonSubTypes.Type(value = RatingResponseQuestionDto.class, name = "RATING"),
        @JsonSubTypes.Type(value = ShortAnswerResponseQuestionDto.class, name = "SHORT_ANSWER"),
        @JsonSubTypes.Type(value = TickBoxGridResponseQuestionDto.class, name = "TICK_BOX_GRID"),
        @JsonSubTypes.Type(value = TimeResponseQuestionDto.class, name = "TIME")
})
@Getter
@Setter
@NoArgsConstructor
public class ResponseQuestionDto<TRes extends ResponseByQuestionResponse> {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long questionId;

    private QuestionType questionType;

    private List<TRes> responses;

    @JsonProperty(value = "@class")
    private String cls = getClass().getName();
}
