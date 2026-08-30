package com.sougata.form_engine.dto.question.schemaRequest;

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
        @JsonSubTypes.Type(value = CheckboxPutReqDto.class, name = "CHECKBOX"),
        @JsonSubTypes.Type(value = DatePutReqDto.class, name = "DATE"),
        @JsonSubTypes.Type(value = DateTimePutReqDto.class, name = "DATE_TIME"),
        @JsonSubTypes.Type(value = DropdownPutReqDto.class, name = "DROPDOWN"),
        @JsonSubTypes.Type(value = DurationPutReqDto.class, name = "DURATION"),
        @JsonSubTypes.Type(value = FileUploadPutReqDto.class, name = "FILE_UPLOAD"),
        @JsonSubTypes.Type(value = LinearScalePutReqDto.class, name = "LINEAR_SCALE"),
        @JsonSubTypes.Type(value = MultipleChoicePutReqDto.class, name = "MULTIPLE_CHOICE"),
        @JsonSubTypes.Type(value = MultipleChoiceGridPutReqDto.class, name = "MULTIPLE_CHOICE_GRID"),
        @JsonSubTypes.Type(value = ParagraphPutReqDto.class, name = "PARAGRAPH"),
        @JsonSubTypes.Type(value = RatingPutReqDto.class, name = "RATING"),
        @JsonSubTypes.Type(value = ShortAnswerPutReqDto.class, name = "SHORT_ANSWER"),
        @JsonSubTypes.Type(value = TickBoxGridPutReqDto.class, name = "TICK_BOX_GRID"),
        @JsonSubTypes.Type(value = TimePutReqDto.class, name = "TIME")
})
@NoArgsConstructor
@Getter
@Setter
public class QuestionPutReqDto {

    private String question;

    private String description;

    @NotNull
    private Boolean required;

    @NotNull
    private QuestionType questionType;

    @JsonProperty(value = "@class")
    private String cls = getClass().getName();

}
