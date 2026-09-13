package com.sougata.form_engine.dto.question.schemaupdatereq;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sougata.form_engine.constant.QuestionType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.Set;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "questionType",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CheckboxUpdateReqDto.class, name = "CHECKBOX"),
        @JsonSubTypes.Type(value = DateUpdateReqDto.class, name = "DATE"),
        @JsonSubTypes.Type(value = DateTimeUpdateReqDto.class, name = "DATE_TIME"),
        @JsonSubTypes.Type(value = DropdownUpdateReqDto.class, name = "DROPDOWN"),
        @JsonSubTypes.Type(value = DurationUpdateReqDto.class, name = "DURATION"),
        @JsonSubTypes.Type(value = FileUploadUpdateReqDto.class, name = "FILE_UPLOAD"),
        @JsonSubTypes.Type(value = LinearScaleUpdateReqDto.class, name = "LINEAR_SCALE"),
        @JsonSubTypes.Type(value = MultipleChoiceUpdateReqDto.class, name = "MULTIPLE_CHOICE"),
        @JsonSubTypes.Type(value = MultipleChoiceGridUpdateReqDto.class, name = "MULTIPLE_CHOICE_GRID"),
        @JsonSubTypes.Type(value = ParagraphUpdateReqDto.class, name = "PARAGRAPH"),
        @JsonSubTypes.Type(value = RatingUpdateReqDto.class, name = "RATING"),
        @JsonSubTypes.Type(value = ShortAnswerUpdateReqDto.class, name = "SHORT_ANSWER"),
        @JsonSubTypes.Type(value = TickBoxGridUpdateReqDto.class, name = "TICK_BOX_GRID"),
        @JsonSubTypes.Type(value = TimeUpdateReqDto.class, name = "TIME")
})
@NoArgsConstructor
@Getter
@Setter
@FieldNameConstants
public class QuestionUpdateReqDto {

    private String question;

    private String description;

    private Boolean required;

    private QuestionType questionType;

    @NotNull
    private Set<@NotNull String> updateFields;

    @JsonProperty(value = "@class")
    private String cls = getClass().getName();

}
