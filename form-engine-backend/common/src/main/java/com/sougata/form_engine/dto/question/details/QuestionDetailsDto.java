package com.sougata.form_engine.dto.question.details;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sougata.form_engine.constant.QuestionType;
import lombok.AllArgsConstructor;
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
        @JsonSubTypes.Type(value = CheckboxDetailsDto.class, name = "CHECKBOX"),
        @JsonSubTypes.Type(value = DateDetailsDto.class, name = "DATE"),
        @JsonSubTypes.Type(value = DateTimeDetailsDto.class, name = "DATE_TIME"),
        @JsonSubTypes.Type(value = DropdownDetailsDto.class, name = "DROPDOWN"),
        @JsonSubTypes.Type(value = DurationDetailsDto.class, name = "DURATION"),
        @JsonSubTypes.Type(value = FileUploadDetailsDto.class, name = "FILE_UPLOAD"),
        @JsonSubTypes.Type(value = LinearScaleDetailsDto.class, name = "LINEAR_SCALE"),
        @JsonSubTypes.Type(value = MultipleChoiceDetailsDto.class, name = "MULTIPLE_CHOICE"),
        @JsonSubTypes.Type(value = MultipleChoiceGridDetailsDto.class, name = "MULTIPLE_CHOICE_GRID"),
        @JsonSubTypes.Type(value = ParagraphDetailsDto.class, name = "PARAGRAPH"),
        @JsonSubTypes.Type(value = RatingDetailsDto.class, name = "RATING"),
        @JsonSubTypes.Type(value = ShortAnswerDetailsDto.class, name = "SHORT_ANSWER"),
        @JsonSubTypes.Type(value = TickBoxGridDetailsDto.class, name = "TICK_BOX_GRID"),
        @JsonSubTypes.Type(value = TimeDetailsDto.class, name = "TIME")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class QuestionDetailsDto {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String question;
    private String description;
    private Boolean required;
    private Integer orderIndex;
    private QuestionType questionType;

    @JsonProperty(value = "@class")
    private String cls = getClass().getName();
}
