package com.sougata.form_data_service.configuration;

import com.sougata.form_data_service.formValidation.responseValidator.CheckboxValidator;
import com.sougata.form_data_service.formValidation.responseValidator.ParagraphValidator;
import com.sougata.form_data_service.formValidation.responseValidator.ShortAnswerValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResponseValidatorConfiguration {

    @Bean("CHECKBOX_SELECT_AT_LEAST_RESPONSE_VALIDATOR")
    public CheckboxValidator.SelectAtLeast checkboxSelectAtLeast() {
        return new CheckboxValidator.SelectAtLeast();
    }

    @Bean("CHECKBOX_SELECT_AT_MOST_RESPONSE_VALIDATOR")
    public CheckboxValidator.SelectAtMost checkboxSelectAtMost() {
        return new CheckboxValidator.SelectAtMost();
    }

    @Bean("CHECKBOX_SELECT_EXACTLY_RESPONSE_VALIDATOR")
    public CheckboxValidator.SelectExactly checkboxSelectExactly() {
        return new CheckboxValidator.SelectExactly();
    }

    @Bean("CHECKBOX_NONE_RESPONSE_VALIDATOR")
    public CheckboxValidator.None checkboxNone() {
        return new CheckboxValidator.None();
    }

    @Bean("SHORT_ANSWER_LENGTH_MAX_CHARACTER_COUNT_RESPONSE_VALIDATOR")
    public ShortAnswerValidator.LengthMaxCharacterCount shortAnswerLengthMaxCharacterCount() {
        return new ShortAnswerValidator.LengthMaxCharacterCount();
    }

    @Bean("SHORT_ANSWER_LENGTH_MIN_CHARACTER_COUNT_RESPONSE_VALIDATOR")
    public ShortAnswerValidator.LengthMinCharacterCount shortAnswerLengthMinCharacterCount() {
        return new ShortAnswerValidator.LengthMinCharacterCount();
    }

    @Bean("SHORT_ANSWER_NUMBER_IS_NUMBER_RESPONSE_VALIDATOR")
    public ShortAnswerValidator.NumberIsNumber shortAnswerNumberIsNumber() {
        return new ShortAnswerValidator.NumberIsNumber();
    }

    @Bean("SHORT_ANSWER_NUMBER_GREATER_THAN_RESPONSE_VALIDATOR")
    public ShortAnswerValidator.NumberGreaterThan shortAnswerNumberGreaterThan() {
        return new ShortAnswerValidator.NumberGreaterThan();
    }

    @Bean("SHORT_ANSWER_NUMBER_GREATER_THAN_OR_EQUAL_TO_RESPONSE_VALIDATOR")
    public ShortAnswerValidator.NumberGreaterThanOrEqualTo shortAnswerNumberGreaterThanOrEqualTo() {
        return new ShortAnswerValidator.NumberGreaterThanOrEqualTo();
    }

    @Bean("SHORT_ANSWER_NUMBER_LESS_THAN_RESPONSE_VALIDATOR")
    public ShortAnswerValidator.NumberLessThan shortAnswerNumberLessThan() {
        return new ShortAnswerValidator.NumberLessThan();
    }

    @Bean("SHORT_ANSWER_NUMBER_LESS_THAN_OR_EQUAL_TO_RESPONSE_VALIDATOR")
    public ShortAnswerValidator.NumberLessThanOrEqualTo shortAnswerNumberLessThanOrEqualTo() {
        return new ShortAnswerValidator.NumberLessThanOrEqualTo();
    }

    @Bean("SHORT_ANSWER_NUMBER_EQUAL_TO_RESPONSE_VALIDATOR")
    public ShortAnswerValidator.NumberEqualTo shortAnswerNumberEqualTo() {
        return new ShortAnswerValidator.NumberEqualTo();
    }

    @Bean("SHORT_ANSWER_NUMBER_NOT_EQUAL_TO_RESPONSE_VALIDATOR")
    public ShortAnswerValidator.NumberNotEqualTo shortAnswerNumberNotEqualTo() {
        return new ShortAnswerValidator.NumberNotEqualTo();
    }

    @Bean("SHORT_ANSWER_NUMBER_BETWEEN_RESPONSE_VALIDATOR")
    public ShortAnswerValidator.NumberBetween shortAnswerNumberBetween() {
        return new ShortAnswerValidator.NumberBetween();
    }

    @Bean("SHORT_ANSWER_NUMBER_NOT_BETWEEN_RESPONSE_VALIDATOR")
    public ShortAnswerValidator.NumberNotBetween shortAnswerNumberNotBetween() {
        return new ShortAnswerValidator.NumberNotBetween();
    }

    @Bean("SHORT_ANSWER_NUMBER_WHOLE_NUMBER_RESPONSE_VALIDATOR")
    public ShortAnswerValidator.NumberWholeNumber shortAnswerNumberWholeNumber() {
        return new ShortAnswerValidator.NumberWholeNumber();
    }

    @Bean("SHORT_ANSWER_REGEX_MATCHES_RESPONSE_VALIDATOR")
    public ShortAnswerValidator.RegexMatches shortAnswerRegexMatches() {
        return new ShortAnswerValidator.RegexMatches();
    }

    @Bean("SHORT_ANSWER_REGEX_DOES_NOT_MATCH_RESPONSE_VALIDATOR")
    public ShortAnswerValidator.RegexDoesNotMatch shortAnswerRegexDoesNotMatch() {
        return new ShortAnswerValidator.RegexDoesNotMatch();
    }

    @Bean("SHORT_ANSWER_TEXT_CONTAINS_RESPONSE_VALIDATOR")
    public ShortAnswerValidator.TextContains shortAnswerTextContains() {
        return new ShortAnswerValidator.TextContains();
    }

    @Bean("SHORT_ANSWER_TEXT_DOES_NOT_CONTAINS_RESPONSE_VALIDATOR")
    public ShortAnswerValidator.TextDoesNotContains shortAnswerTextDoesNotContains() {
        return new ShortAnswerValidator.TextDoesNotContains();
    }

    @Bean("SHORT_ANSWER_TEXT_EMAIL_RESPONSE_VALIDATOR")
    public ShortAnswerValidator.TextEmail shortAnswerTextEmail() {
        return new ShortAnswerValidator.TextEmail();
    }

    @Bean("SHORT_ANSWER_TEXT_URL_RESPONSE_VALIDATOR")
    public ShortAnswerValidator.TextUrl shortAnswerTextUrl() {
        return new ShortAnswerValidator.TextUrl();
    }

    @Bean("SHORT_ANSWER_NONE_RESPONSE_VALIDATOR")
    public ShortAnswerValidator.None shortAnswerNone() {
        return new ShortAnswerValidator.None();
    }

    @Bean("PARAGRAPH_LENGTH_MAX_CHARACTER_COUNT_RESPONSE_VALIDATOR")
    public ParagraphValidator.LengthMaxCharacterCount paragraphLengthMaxCharacterCount() {
        return new ParagraphValidator.LengthMaxCharacterCount();
    }

    @Bean("PARAGRAPH_LENGTH_MIN_CHARACTER_COUNT_RESPONSE_VALIDATOR")
    public ParagraphValidator.LengthMinCharacterCount paragraphLengthMinCharacterCount() {
        return new ParagraphValidator.LengthMinCharacterCount();
    }

    @Bean("PARAGRAPH_REGEX_MATCHES_RESPONSE_VALIDATOR")
    public ParagraphValidator.RegexMatches paragraphRegexMatches() {
        return new ParagraphValidator.RegexMatches();
    }

    @Bean("PARAGRAPH_DOES_NOT_MATCH_RESPONSE_VALIDATOR")
    public ParagraphValidator.DoesNotMatch paragraphDoesNotMatch() {
        return new ParagraphValidator.DoesNotMatch();
    }

    @Bean("PARAGRAPH_NONE_RESPONSE_VALIDATOR")
    public ParagraphValidator.None paragraphNone() {
        return new ParagraphValidator.None();
    }

}
