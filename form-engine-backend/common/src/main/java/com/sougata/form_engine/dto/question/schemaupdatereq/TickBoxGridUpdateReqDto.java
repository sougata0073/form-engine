package com.sougata.form_engine.dto.question.schemaupdatereq;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sougata.form_engine.constant.ComplexQuestionUpdateAction;
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
public class TickBoxGridUpdateReqDto extends QuestionUpdateReqDto {

    private Boolean eachRowRequired;

    @Valid
    private Row row;

    @Valid
    private Column column;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Row {

        private Long id;

        private @Length(min = 1) String row;

        @NotNull
        private ComplexQuestionUpdateAction action;

        @AssertTrue
        @JsonIgnore
        public boolean isFieldsStateMatchingWithAction() {
            return (action == ComplexQuestionUpdateAction.ADD && id == null && row != null) ||
                    (action == ComplexQuestionUpdateAction.UPDATE && id != null && row != null) ||
                    (action == ComplexQuestionUpdateAction.DELETE && id != null && row == null);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Column {

        private Long id;

        private @Length(min = 1) String column;

        @NotNull
        private ComplexQuestionUpdateAction action;

        @AssertTrue
        @JsonIgnore
        public boolean isFieldsStateMatchingWithAction() {
            return (action == ComplexQuestionUpdateAction.ADD && id == null && column != null) ||
                    (action == ComplexQuestionUpdateAction.UPDATE && id != null && column != null) ||
                    (action == ComplexQuestionUpdateAction.DELETE && id != null && column == null);
        }
    }

}
