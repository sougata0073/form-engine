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
        name = "date_time_responses",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "date_time_uk_date_time_question_id",
                        columnNames = {
                                "date_time",
                                "question_id"
                        }
                )
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DateTimeResponse extends AnyTypeQuestionResponse {

    @Column(nullable = false)
    private Instant dateTime;

}
