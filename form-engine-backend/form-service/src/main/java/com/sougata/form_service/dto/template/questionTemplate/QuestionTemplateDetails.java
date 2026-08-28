package com.sougata.form_service.dto.template.questionTemplate;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sougata.form_service.constant.QuestionType;
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
        @JsonSubTypes.Type(value = CheckboxTemplateDetails.class, name = "CHECKBOX"),
        @JsonSubTypes.Type(value = DateTemplateDetails.class, name = "DATE"),
        @JsonSubTypes.Type(value = DateTimeTemplateDetails.class, name = "DATE_TIME"),
        @JsonSubTypes.Type(value = DropdownTemplateDetails.class, name = "DROPDOWN"),
        @JsonSubTypes.Type(value = DurationTemplateDetails.class, name = "DURATION"),
        @JsonSubTypes.Type(value = FileUploadTemplateDetails.class, name = "FILE_UPLOAD"),
        @JsonSubTypes.Type(value = LinearScaleTemplateDetails.class, name = "LINEAR_SCALE"),
        @JsonSubTypes.Type(value = MultipleChoiceTemplateDetails.class, name = "MULTIPLE_CHOICE"),
        @JsonSubTypes.Type(value = MultipleChoiceGridTemplateDetails.class, name = "MULTIPLE_CHOICE_GRID"),
        @JsonSubTypes.Type(value = ParagraphTemplateDetails.class, name = "PARAGRAPH"),
        @JsonSubTypes.Type(value = RatingTemplateDetails.class, name = "RATING"),
        @JsonSubTypes.Type(value = ShortAnswerTemplateDetails.class, name = "SHORT_ANSWER"),
        @JsonSubTypes.Type(value = TickBoxGridTemplateDetails.class, name = "TICK_BOX_GRID"),
        @JsonSubTypes.Type(value = TimeTemplateDetails.class, name = "TIME")
})
@NoArgsConstructor
@Getter
@Setter
public class QuestionTemplateDetails {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String question;
    private String description;
    private Boolean required;
    private Integer orderIndex;
    private QuestionType questionType;
}
