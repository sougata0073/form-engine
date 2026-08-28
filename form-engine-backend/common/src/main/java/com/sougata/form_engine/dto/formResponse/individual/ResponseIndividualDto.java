package com.sougata.form_engine.dto.formResponse.individual;

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
        @JsonSubTypes.Type(value = CheckboxResponseIndividualDto.class, name = "CHECKBOX"),
        @JsonSubTypes.Type(value = DateResponseIndividualDto.class, name = "DATE"),
        @JsonSubTypes.Type(value = DateTimeResponseIndividualDto.class, name = "DATE_TIME"),
        @JsonSubTypes.Type(value = DropdownResponseIndividualDto.class, name = "DROPDOWN"),
        @JsonSubTypes.Type(value = DurationResponseIndividualDto.class, name = "DURATION"),
        @JsonSubTypes.Type(value = FileUploadResponseIndividualDto.class, name = "FILE_UPLOAD"),
        @JsonSubTypes.Type(value = LinearScaleResponseIndividualDto.class, name = "LINEAR_SCALE"),
        @JsonSubTypes.Type(value = MultipleChoiceResponseIndividualDto.class, name = "MULTIPLE_CHOICE"),
        @JsonSubTypes.Type(value = MultipleChoiceGridResponseIndividualDto.class, name = "MULTIPLE_CHOICE_GRID"),
        @JsonSubTypes.Type(value = ParagraphResponseIndividualDto.class, name = "PARAGRAPH"),
        @JsonSubTypes.Type(value = RatingResponseIndividualDto.class, name = "RATING"),
        @JsonSubTypes.Type(value = ShortAnswerResponseIndividualDto.class, name = "SHORT_ANSWER"),
        @JsonSubTypes.Type(value = TickBoxGridResponseIndividualDto.class, name = "TICK_BOX_GRID"),
        @JsonSubTypes.Type(value = TimeResponseIndividualDto.class, name = "TIME")
})
@Getter
@Setter
@NoArgsConstructor
public class ResponseIndividualDto {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long questionId;

    private QuestionType questionType;

    @JsonProperty(value = "@class")
    private String cls = getClass().getName();

}
