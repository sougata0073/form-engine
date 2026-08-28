package com.sougata.form_service.validation.configuration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sougata.form_service.constant.ValidationId;
import com.sougata.form_service.constant.ValidationMessages;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class CheckboxValidationConfig {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class SelectAtLeast extends ValidationConfig {
        private Integer number;

        @AssertTrue(message = ValidationMessages.CHECKBOX_SELECT_AT_LEAST_REQUIRED)
        @JsonIgnore
        public boolean isSelectAtLeastNumberValid() {
            return (getValidationId() == ValidationId.CHECKBOX_NONE && number == null) ||
                    (getValidationId() != ValidationId.CHECKBOX_NONE && number != null);
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class SelectAtMost extends ValidationConfig {
        private Integer number;

        @AssertTrue(message = ValidationMessages.CHECKBOX_SELECT_AT_MOST_REQUIRED)
        @JsonIgnore
        public boolean isSelectAtMostNumberValid() {
            return (getValidationId() == ValidationId.CHECKBOX_NONE && number == null) ||
                    (getValidationId() != ValidationId.CHECKBOX_NONE && number != null);
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class SelectExactly extends ValidationConfig {
        private Integer number;

        @AssertTrue(message = ValidationMessages.CHECKBOX_SELECT_EXACTLY_REQUIRED)
        @JsonIgnore
        public boolean isSelectExactlyNumberValid() {
            return (getValidationId() == ValidationId.CHECKBOX_NONE && number == null) ||
                    (getValidationId() != ValidationId.CHECKBOX_NONE && number != null);
        }
    }

}
