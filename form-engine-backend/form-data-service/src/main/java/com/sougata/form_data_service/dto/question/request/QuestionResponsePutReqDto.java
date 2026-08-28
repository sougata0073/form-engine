package com.sougata.form_data_service.dto.question.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sougata.form_data_service.constant.QuestionType;
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
        @JsonSubTypes.Type(value = CheckboxResponsePutReqDto.class, name = "CHECKBOX"),
        @JsonSubTypes.Type(value = DateResponsePutReqDto.class, name = "DATE"),
        @JsonSubTypes.Type(value = DateTimeResponsePutReqDto.class, name = "DATE_TIME"),
        @JsonSubTypes.Type(value = DropdownResponsePutReqDto.class, name = "DROPDOWN"),
        @JsonSubTypes.Type(value = DurationResponsePutReqDto.class, name = "DURATION"),
        @JsonSubTypes.Type(value = FileUploadResponsePutReqDto.class, name = "FILE_UPLOAD"),
        @JsonSubTypes.Type(value = LinearScaleResponsePutReqDto.class, name = "LINEAR_SCALE"),
        @JsonSubTypes.Type(value = MultipleChoiceResponsePutReqDto.class, name = "MULTIPLE_CHOICE"),
        @JsonSubTypes.Type(value = MultipleChoiceGridResponsePutReqDto.class, name = "MULTIPLE_CHOICE_GRID"),
        @JsonSubTypes.Type(value = ParagraphResponsePutReqDto.class, name = "PARAGRAPH"),
        @JsonSubTypes.Type(value = RatingResponsePutReqDto.class, name = "RATING"),
        @JsonSubTypes.Type(value = ShortAnswerResponsePutReqDto.class, name = "SHORT_ANSWER"),
        @JsonSubTypes.Type(value = TickBoxGridResponsePutReqDto.class, name = "TICK_BOX_GRID"),
        @JsonSubTypes.Type(value = TimeResponsePutReqDto.class, name = "TIME")
})
@Getter
@Setter
@NoArgsConstructor
public class QuestionResponsePutReqDto {

    @NotNull
    private Long questionId;

    @NotNull
    private QuestionType questionType;

    @JsonProperty(value = "@class")
    private String cls = getClass().getName();

}
