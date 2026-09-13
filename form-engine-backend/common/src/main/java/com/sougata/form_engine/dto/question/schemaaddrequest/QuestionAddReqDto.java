package com.sougata.form_engine.dto.question.schemaaddrequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sougata.form_engine.constant.QuestionType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "questionType",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CheckboxAddReqDto.class, name = "CHECKBOX"),
        @JsonSubTypes.Type(value = DateAddReqDto.class, name = "DATE"),
        @JsonSubTypes.Type(value = DateTimeAddReqDto.class, name = "DATE_TIME"),
        @JsonSubTypes.Type(value = DropdownAddReqDto.class, name = "DROPDOWN"),
        @JsonSubTypes.Type(value = DurationAddReqDto.class, name = "DURATION"),
        @JsonSubTypes.Type(value = FileUploadAddReqDto.class, name = "FILE_UPLOAD"),
        @JsonSubTypes.Type(value = LinearScaleAddReqDto.class, name = "LINEAR_SCALE"),
        @JsonSubTypes.Type(value = MultipleChoiceAddReqDto.class, name = "MULTIPLE_CHOICE"),
        @JsonSubTypes.Type(value = MultipleChoiceGridAddReqDto.class, name = "MULTIPLE_CHOICE_GRID"),
        @JsonSubTypes.Type(value = ParagraphAddReqDto.class, name = "PARAGRAPH"),
        @JsonSubTypes.Type(value = RatingAddReqDto.class, name = "RATING"),
        @JsonSubTypes.Type(value = ShortAnswerAddReqDto.class, name = "SHORT_ANSWER"),
        @JsonSubTypes.Type(value = TickBoxGridAddReqDto.class, name = "TICK_BOX_GRID"),
        @JsonSubTypes.Type(value = TimeAddReqDto.class, name = "TIME")
})
@NoArgsConstructor
@Getter
@Setter
public class QuestionAddReqDto {

    private String question;

    private String description;

    @NotNull
    private Boolean required;

    @NotNull
    private QuestionType questionType;

    @JsonProperty(value = "@class")
    private String cls = getClass().getName();

}
