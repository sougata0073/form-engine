package com.sougata.form_service.validation.configuration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sougata.form_service.constant.ValidationId;
import com.sougata.form_service.constant.ValidationMessages;
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

        @AssertTrue(message = ValidationMessages.SHORT_ANSWER_FROM_NUMBER_REQUIRED)
        @JsonIgnore
        public boolean isBetweenFromNumberValid() {
            return (getValidationId() == ValidationId.SHORT_ANSWER_NONE && fromNumber == null) ||
                    (getValidationId() != ValidationId.SHORT_ANSWER_NONE && fromNumber != null);
        }

        @AssertTrue(message = ValidationMessages.SHORT_ANSWER_TO_NUMBER_REQUIRED)
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

        @AssertTrue(message = ValidationMessages.SHORT_ANSWER_NUMBER_REQUIRED)
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

        @AssertTrue(message = ValidationMessages.SHORT_ANSWER_NUMBER_REQUIRED)
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

        @AssertTrue(message = ValidationMessages.SHORT_ANSWER_NUMBER_REQUIRED)
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

        @AssertTrue(message = ValidationMessages.SHORT_ANSWER_NUMBER_REQUIRED)
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

        @AssertTrue(message = ValidationMessages.SHORT_ANSWER_NUMBER_REQUIRED)
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

        @AssertTrue(message = ValidationMessages.SHORT_ANSWER_FROM_NUMBER_REQUIRED)
        @JsonIgnore
        public boolean isNotBetweenFromNumberValid() {
            return (getValidationId() == ValidationId.SHORT_ANSWER_NONE && fromNumber == null) ||
                    (getValidationId() != ValidationId.SHORT_ANSWER_NONE && fromNumber != null);
        }

        @AssertTrue(message = ValidationMessages.SHORT_ANSWER_TO_NUMBER_REQUIRED)
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

        @AssertTrue(message = ValidationMessages.SHORT_ANSWER_NUMBER_REQUIRED)
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

        @AssertTrue(message = ValidationMessages.SHORT_ANSWER_PATTERN_REQUIRED)
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

        @AssertTrue(message = ValidationMessages.SHORT_ANSWER_PATTERN_REQUIRED)
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

        @AssertTrue(message = ValidationMessages.SHORT_ANSWER_CONTAINS_VALUE_REQUIRED)
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

        @AssertTrue(message = ValidationMessages.SHORT_ANSWER_NOT_CONTAINS_VALUE_REQUIRED)
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

        @AssertTrue(message = ValidationMessages.SHORT_ANSWER_MAX_CHARACTER_COUNT_REQUIRED)
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

        @AssertTrue(message = ValidationMessages.SHORT_ANSWER_MIN_CHARACTER_COUNT_REQUIRED)
        @JsonIgnore
        public boolean isMinCharacterCountMinCountValid() {
            return (getValidationId() == ValidationId.SHORT_ANSWER_NONE && number == null) ||
                    (getValidationId() != ValidationId.SHORT_ANSWER_NONE && number != null);
        }
    }

}
