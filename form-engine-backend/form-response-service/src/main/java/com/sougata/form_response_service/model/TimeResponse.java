package com.sougata.form_response_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(
        name = "time_responses",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "time_uk_time_question_id",
                        columnNames = {
                                "time",
                                "question_id"
                        }
                )
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeResponse extends AnyTypeQuestionResponse {

    @Column(nullable = false)
    private Instant time;

}
