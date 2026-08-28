package com.sougata.form_engine.dto.validation.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sougata.form_engine.constant.ValidationId;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ParagraphValidationConfig {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class MaxCharacterCount extends ValidationConfig {
        private Integer number;

        @AssertTrue
        @JsonIgnore
        public boolean isMaxCharacterCountMaxNumberValid() {
            return (getValidationId() == ValidationId.PARAGRAPH_NONE && number == null) ||
                    (getValidationId() != ValidationId.PARAGRAPH_NONE && number != null);
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class MinCharacterCount extends ValidationConfig {
        private Integer number;

        @AssertTrue
        @JsonIgnore
        public boolean isMinCharacterCountMinNumberValid() {
            return (getValidationId() == ValidationId.PARAGRAPH_NONE && number == null) ||
                    (getValidationId() != ValidationId.PARAGRAPH_NONE && number != null);
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Matches extends ValidationConfig {
        private String text;

        @AssertTrue
        @JsonIgnore
        public boolean isMatchesPatternValid() {
            return (getValidationId() == ValidationId.PARAGRAPH_NONE && text == null) ||
                    (getValidationId() != ValidationId.PARAGRAPH_NONE && text != null);
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class DoesNotMatch extends ValidationConfig {
        private String text;

        @AssertTrue
        @JsonIgnore
        public boolean isDoesNotMatchesPatternValid() {
            return (getValidationId() == ValidationId.PARAGRAPH_NONE && text == null) ||
                    (getValidationId() != ValidationId.PARAGRAPH_NONE && text != null);
        }
    }

}
