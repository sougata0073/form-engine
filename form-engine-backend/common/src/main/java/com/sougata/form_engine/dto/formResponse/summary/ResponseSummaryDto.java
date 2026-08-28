package com.sougata.form_engine.dto.formResponse.summary;

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
        @JsonSubTypes.Type(value = CheckboxResponseSummaryDto.class, name = "CHECKBOX"),
        @JsonSubTypes.Type(value = DateResponseSummaryDto.class, name = "DATE"),
        @JsonSubTypes.Type(value = DateTimeResponseSummaryDto.class, name = "DATE_TIME"),
        @JsonSubTypes.Type(value = DropdownResponseSummaryDto.class, name = "DROPDOWN"),
        @JsonSubTypes.Type(value = DurationResponseSummaryDto.class, name = "DURATION"),
        @JsonSubTypes.Type(value = FileUploadResponseSummaryDto.class, name = "FILE_UPLOAD"),
        @JsonSubTypes.Type(value = LinearScaleResponseSummaryDto.class, name = "LINEAR_SCALE"),
        @JsonSubTypes.Type(value = MultipleChoiceResponseSummaryDto.class, name = "MULTIPLE_CHOICE"),
        @JsonSubTypes.Type(value = MultipleChoiceGridResponseSummaryDto.class, name = "MULTIPLE_CHOICE_GRID"),
        @JsonSubTypes.Type(value = ParagraphResponseSummaryDto.class, name = "PARAGRAPH"),
        @JsonSubTypes.Type(value = RatingResponseSummaryDto.class, name = "RATING"),
        @JsonSubTypes.Type(value = ShortAnswerResponseSummaryDto.class, name = "SHORT_ANSWER"),
        @JsonSubTypes.Type(value = TickBoxGridResponseSummaryDto.class, name = "TICK_BOX_GRID"),
        @JsonSubTypes.Type(value = TimeResponseSummaryDto.class, name = "TIME")
})
@Getter
@Setter
@NoArgsConstructor
public class ResponseSummaryDto<TResponse> {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long questionId;

    private String question;

    private Integer orderIndex;

    private QuestionType questionType;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long numberOfResponses;

    private List<TResponse> responses;

    @JsonProperty(value = "@class")
    private String cls = getClass().getName();
}
