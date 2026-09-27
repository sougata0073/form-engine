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
        name = "duration_responses",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "duration_uk_hours_minutes_seconds_question_id",
                        columnNames = {
                                "hours",
                                "minutes",
                                "seconds",
                                "question_id"
                        }
                )
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DurationResponse extends AnyTypeQuestionResponse {

    @Column(nullable = false)
    private Integer hours;

    @Column(nullable = false)
    private Integer minutes;

    @Column(nullable = false)
    private Integer seconds;

}
