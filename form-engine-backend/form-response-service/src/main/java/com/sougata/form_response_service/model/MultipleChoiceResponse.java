package com.sougata.form_response_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "multiple_choice_responses")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldNameConstants
@DynamicUpdate
public class MultipleChoiceResponse extends AnyTypeQuestionResponse {

        @Column(nullable = false)
        private Long optionId;

}
