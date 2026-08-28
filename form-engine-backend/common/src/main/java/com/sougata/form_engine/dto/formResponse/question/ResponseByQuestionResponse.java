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
        @JsonSubTypes.Type(value = CheckboxResponseQuestionDto.Response.class, name = "CHECKBOX"),
        @JsonSubTypes.Type(value = DateResponseQuestionDto.Response.class, name = "DATE"),
        @JsonSubTypes.Type(value = DateTimeResponseQuestionDto.Response.class, name = "DATE_TIME"),
        @JsonSubTypes.Type(value = DropdownResponseQuestionDto.Response.class, name = "DROPDOWN"),
        @JsonSubTypes.Type(value = DurationResponseQuestionDto.Response.class, name = "DURATION"),
        @JsonSubTypes.Type(value = FileUploadResponseQuestionDto.Response.class, name = "FILE_UPLOAD"),
        @JsonSubTypes.Type(value = LinearScaleResponseQuestionDto.Response.class, name = "LINEAR_SCALE"),
        @JsonSubTypes.Type(value = MultipleChoiceResponseQuestionDto.Response.class, name = "MULTIPLE_CHOICE"),
        @JsonSubTypes.Type(value = MultipleChoiceGridResponseQuestionDto.Response.class, name = "MULTIPLE_CHOICE_GRID"),
        @JsonSubTypes.Type(value = ParagraphResponseQuestionDto.Response.class, name = "PARAGRAPH"),
        @JsonSubTypes.Type(value = RatingResponseQuestionDto.Response.class, name = "RATING"),
        @JsonSubTypes.Type(value = ShortAnswerResponseQuestionDto.Response.class, name = "SHORT_ANSWER"),
        @JsonSubTypes.Type(value = TickBoxGridResponseQuestionDto.Response.class, name = "TICK_BOX_GRID"),
        @JsonSubTypes.Type(value = TimeResponseQuestionDto.Response.class, name = "TIME")
})
@Getter
@Setter
@NoArgsConstructor
public class ResponseByQuestionResponse {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long questionId;

    private QuestionType questionType;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long responseCount;

    private String formResponsesIdentifier;

    @JsonProperty(value = "@class")
    private String cls = getClass().getName();
}
