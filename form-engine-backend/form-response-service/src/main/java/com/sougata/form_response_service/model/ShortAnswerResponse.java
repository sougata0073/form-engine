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
        name = "short_answer_responses",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "short_answer_uk_text_question_id",
                        columnNames = {
                                "text",
                                "question_id"
                        }
                )
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShortAnswerResponse extends AnyTypeQuestionResponse {

        @Column(nullable = false, columnDefinition = "text")
        private String text;


}
