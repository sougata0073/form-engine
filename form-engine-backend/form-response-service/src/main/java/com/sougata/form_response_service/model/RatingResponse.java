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
        name = "rating_responses",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "rating_uk_rating_question_id",
                        columnNames = {
                                "rating",
                                "question_id"
                        }
                )
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingResponse extends AnyTypeQuestionResponse {

        @Column(nullable = false)
        private Integer rating;


}
