package com.sougata.form_engine.dto.validation.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sougata.form_engine.constant.ValidationId;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ShortAnswerValidationConfig {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Between extends ValidationConfig {
        private Double fromNumber;
        private Double toNumber;

        @AssertTrue
        @JsonIgnore
        public boolean isBetweenFromNumberValid() {
            return (getValidationId() == ValidationId.SHORT_ANSWER_NONE && fromNumber == null) ||
                    (getValidationId() != ValidationId.SHORT_ANSWER_NONE && fromNumber != null);
        }

        @AssertTrue
        @JsonIgnore
        public boolean isBetweenToNumberValid() {
            return (getValidationId() == ValidationId.SHORT_ANSWER_NONE && toNumber == null) ||
                    (getValidationId() != ValidationId.SHORT_ANSWER_NONE && toNumber != null);
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class EqualTo extends ValidationConfig {
        private Double number;

        @AssertTrue
        @JsonIgnore
        public boolean isEqualToNumberValid() {
            return (getValidationId() == ValidationId.SHORT_ANSWER_NONE && number == null) ||
                    (getValidationId() != ValidationId.SHORT_ANSWER_NONE && number != null);
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class GreaterThan extends ValidationConfig {
        private Double number;

        @AssertTrue
        @JsonIgnore
        public boolean isGreaterThanNumberValid() {
            return (getValidationId() == ValidationId.SHORT_ANSWER_NONE && number == null) ||
                    (getValidationId() != ValidationId.SHORT_ANSWER_NONE && number != null);
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class GreaterThanOrEqualTo extends ValidationConfig {
        private Double number;

        @AssertTrue
        @JsonIgnore
        public boolean isGreaterThanOrEqualToNumberValid() {
            return (getValidationId() == ValidationId.SHORT_ANSWER_NONE && number == null) ||
                    (getValidationId() != ValidationId.SHORT_ANSWER_NONE && number != null);
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class LessThan extends ValidationConfig {
        private Double number;

        @AssertTrue
        @JsonIgnore
        public boolean isLessThanNumberValid() {
            return (getValidationId() == ValidationId.SHORT_ANSWER_NONE && number == null) ||
                    (getValidationId() != ValidationId.SHORT_ANSWER_NONE && number != null);
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class LessThanOrEqualTo extends ValidationConfig {
        private Double number;

        @AssertTrue
        @JsonIgnore
        public boolean isLessThanOrEqualToNumberValid() {
            return (getValidationId() == ValidationId.SHORT_ANSWER_NONE && number == null) ||
                    (getValidationId() != ValidationId.SHORT_ANSWER_NONE && number != null);
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class NotBetween extends ValidationConfig {
        private Double fromNumber;
        private Double toNumber;

        @AssertTrue
        @JsonIgnore
        public boolean isNotBetweenFromNumberValid() {
            return (getValidationId() == ValidationId.SHORT_ANSWER_NONE && fromNumber == null) ||
                    (getValidationId() != ValidationId.SHORT_ANSWER_NONE && fromNumber != null);
        }

        @AssertTrue
        @JsonIgnore
        public boolean isNotBetweenToNumberValid() {
            return (getValidationId() == ValidationId.SHORT_ANSWER_NONE && toNumber == null) ||
                    (getValidationId() != ValidationId.SHORT_ANSWER_NONE && toNumber != null);
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class NotEqualTo extends ValidationConfig {
        private Double number;

        @AssertTrue
        @JsonIgnore
        public boolean isNotEqualToNumberValid() {
            return (getValidationId() == ValidationId.SHORT_ANSWER_NONE && number == null) ||
                    (getValidationId() != ValidationId.SHORT_ANSWER_NONE && number != null);
        }
    }

    public static class IsNumber extends ValidationConfig {
    }

    public static class WholeNumber extends ValidationConfig {
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
            return (getValidationId() == ValidationId.SHORT_ANSWER_NONE && text == null) ||
                    (getValidationId() != ValidationId.SHORT_ANSWER_NONE && text != null);
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
            return (getValidationId() == ValidationId.SHORT_ANSWER_NONE && text == null) ||
                    (getValidationId() != ValidationId.SHORT_ANSWER_NONE && text != null);
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Contains extends ValidationConfig {
        private String text;

        @AssertTrue
        @JsonIgnore
        public boolean isContainsContainsValid() {
            return (getValidationId() == ValidationId.SHORT_ANSWER_NONE && text == null) ||
                    (getValidationId() != ValidationId.SHORT_ANSWER_NONE && text != null);
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class DoesNotContains extends ValidationConfig {
        private String text;

        @AssertTrue
        @JsonIgnore
        public boolean isDoesNotContainsDoesNotContainsValid() {
            return (getValidationId() == ValidationId.SHORT_ANSWER_NONE && text == null) ||
                    (getValidationId() != ValidationId.SHORT_ANSWER_NONE && text != null);
        }
    }

    public static class Url extends ValidationConfig {
    }

    public static class Email extends ValidationConfig {
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class MaxCharacterCount extends ValidationConfig {
        private Integer number;

        @AssertTrue
        @JsonIgnore
        public boolean isMaxCharacterCountMaxCountValid() {
            return (getValidationId() == ValidationId.SHORT_ANSWER_NONE && number == null) ||
                    (getValidationId() != ValidationId.SHORT_ANSWER_NONE && number != null);
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
        public boolean isMinCharacterCountMinCountValid() {
            return (getValidationId() == ValidationId.SHORT_ANSWER_NONE && number == null) ||
                    (getValidationId() != ValidationId.SHORT_ANSWER_NONE && number != null);
        }
    }

}
