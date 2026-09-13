package com.sougata.form_engine.dto.question.schemaupdatereq;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sougata.form_engine.constant.ComplexQuestionUpdateAction;
import com.sougata.form_engine.dto.validation.config.ValidationConfig;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@Getter
@Setter
@FieldNameConstants
public class CheckboxUpdateReqDto extends QuestionUpdateReqDto {

    @Valid
    private Option option;

    @Valid
    private ValidationConfig validationConfig;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Option {

        private Long id;

        private @Length(min = 1) String option;

        @NotNull
        private ComplexQuestionUpdateAction action;

        @AssertTrue
        @JsonIgnore
        public boolean isFieldsStateMatchingWithAction() {
            return (action == ComplexQuestionUpdateAction.ADD && id == null && option != null) ||
                    (action == ComplexQuestionUpdateAction.UPDATE && id != null && option != null) ||
                    (action == ComplexQuestionUpdateAction.DELETE && id != null && option == null);
        }

    }

}