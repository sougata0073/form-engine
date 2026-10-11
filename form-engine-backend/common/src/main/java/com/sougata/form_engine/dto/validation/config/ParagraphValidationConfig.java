package com.sougata.form_engine.dto.validation.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sougata.form_engine.constant.ValidationId;
import jakarta.validation.constraints.AssertTrue;
import lombok.*;

import java.util.Objects;

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

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            MaxCharacterCount that = (MaxCharacterCount) o;
            return super.equals(that) && Objects.equals(number, that.number);
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

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            MinCharacterCount that = (MinCharacterCount) o;
            return super.equals(that) &&  Objects.equals(number, that.number);
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

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            Matches that = (Matches) o;
            return super.equals(that) && Objects.equals(text, that.text);
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

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            DoesNotMatch that = (DoesNotMatch) o;
            return super.equals(that) &&  Objects.equals(text, that.text);
        }

    }

}
