package com.sougata.form_response_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "linear_scale_responses",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "linear_scale_uk_scale_question_id",
                        columnNames = {
                                "scale",
                                "question_id"
                        }
                )
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LinearScaleResponse extends AnyTypeQuestionResponse {

    @Column(nullable = false)
    private Integer scale;


}
