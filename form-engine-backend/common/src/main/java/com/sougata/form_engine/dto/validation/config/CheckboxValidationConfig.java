package com.sougata.form_engine.dto.validation.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sougata.form_engine.constant.ValidationId;
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

        @AssertTrue
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

        @AssertTrue
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

        @AssertTrue
        @JsonIgnore
        public boolean isSelectExactlyNumberValid() {
            return (getValidationId() == ValidationId.CHECKBOX_NONE && number == null) ||
                    (getValidationId() != ValidationId.CHECKBOX_NONE && number != null);
        }
    }

}
