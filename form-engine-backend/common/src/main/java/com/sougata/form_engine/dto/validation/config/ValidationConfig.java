package com.sougata.form_engine.dto.validation.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sougata.form_engine.constant.ValidationId;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "validationId",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CheckboxValidationConfig.SelectAtLeast.class, name = "CHECKBOX_SELECT_AT_LEAST"),
        @JsonSubTypes.Type(value = CheckboxValidationConfig.SelectAtMost.class, name = "CHECKBOX_SELECT_AT_MOST"),
        @JsonSubTypes.Type(value = CheckboxValidationConfig.SelectExactly.class, name = "CHECKBOX_SELECT_EXACTLY"),
        @JsonSubTypes.Type(value = NoneValidationConfig.class, name = "CHECKBOX_NONE"),
        @JsonSubTypes.Type(value = ParagraphValidationConfig.MaxCharacterCount.class, name = "PARAGRAPH_LENGTH_MAX_CHARACTER_COUNT"),
        @JsonSubTypes.Type(value = ParagraphValidationConfig.MinCharacterCount.class, name = "PARAGRAPH_LENGTH_MIN_CHARACTER_COUNT"),
        @JsonSubTypes.Type(value = ParagraphValidationConfig.Matches.class, name = "PARAGRAPH_REGEX_MATCHES"),
        @JsonSubTypes.Type(value = ParagraphValidationConfig.DoesNotMatch.class, name = "PARAGRAPH_REGEX_DOES_NOT_MATCHES"),
        @JsonSubTypes.Type(value = NoneValidationConfig.class, name = "PARAGRAPH_NONE"),
        @JsonSubTypes.Type(value = ShortAnswerValidationConfig.Between.class, name = "SHORT_ANSWER_NUMBER_BETWEEN"),
        @JsonSubTypes.Type(value = ShortAnswerValidationConfig.EqualTo.class, name = "SHORT_ANSWER_NUMBER_EQUAL_TO"),
        @JsonSubTypes.Type(value = ShortAnswerValidationConfig.GreaterThan.class, name = "SHORT_ANSWER_NUMBER_GREATER_THAN"),
        @JsonSubTypes.Type(value = ShortAnswerValidationConfig.GreaterThanOrEqualTo.class, name = "SHORT_ANSWER_NUMBER_GREATER_THAN_OR_EQUAL_TO"),
        @JsonSubTypes.Type(value = ShortAnswerValidationConfig.LessThan.class, name = "SHORT_ANSWER_NUMBER_LESS_THAN"),
        @JsonSubTypes.Type(value = ShortAnswerValidationConfig.LessThanOrEqualTo.class, name = "SHORT_ANSWER_NUMBER_LESS_THAN_OR_EQUAL_TO"),
        @JsonSubTypes.Type(value = ShortAnswerValidationConfig.NotBetween.class, name = "SHORT_ANSWER_NUMBER_NOT_BETWEEN"),
        @JsonSubTypes.Type(value = ShortAnswerValidationConfig.NotEqualTo.class, name = "SHORT_ANSWER_NUMBER_NOT_EQUAL_TO"),
        @JsonSubTypes.Type(value = ShortAnswerValidationConfig.IsNumber.class, name = "SHORT_ANSWER_NUMBER_IS_NUMBER"),
        @JsonSubTypes.Type(value = ShortAnswerValidationConfig.WholeNumber.class, name = "SHORT_ANSWER_NUMBER_WHOLE_NUMBER"),
        @JsonSubTypes.Type(value = ShortAnswerValidationConfig.Matches.class, name = "SHORT_ANSWER_REGULAR_EXPRESSION_MATCHES"),
        @JsonSubTypes.Type(value = ShortAnswerValidationConfig.DoesNotMatch.class, name = "SHORT_ANSWER_REGULAR_EXPRESSION_DOES_NOT_MATCHES"),
        @JsonSubTypes.Type(value = ShortAnswerValidationConfig.Contains.class, name = "SHORT_ANSWER_TEXT_CONTAINS"),
        @JsonSubTypes.Type(value = ShortAnswerValidationConfig.DoesNotContains.class, name = "SHORT_ANSWER_TEXT_NOT_CONTAINS"),
        @JsonSubTypes.Type(value = ShortAnswerValidationConfig.Url.class, name = "SHORT_ANSWER_TEXT_URL"),
        @JsonSubTypes.Type(value = ShortAnswerValidationConfig.Email.class, name = "SHORT_ANSWER_TEXT_EMAIL"),
        @JsonSubTypes.Type(value = ShortAnswerValidationConfig.MaxCharacterCount.class, name = "SHORT_ANSWER_LENGTH_MAX_CHARACTER_COUNT"),
        @JsonSubTypes.Type(value = ShortAnswerValidationConfig.MinCharacterCount.class, name = "SHORT_ANSWER_LENGTH_MIN_CHARACTER_COUNT"),
        @JsonSubTypes.Type(value = NoneValidationConfig.class, name = "SHORT_ANSWER_NONE")
})
@NoArgsConstructor
@Getter
@Setter
public class ValidationConfig {

    @NotNull
    private ValidationId validationId;

    private String errorText;

    @JsonProperty(value = "@class")
    private String cls = getClass().getName();
}
