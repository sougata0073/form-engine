package com.sougata.form_response_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Entity
@Table(name = "form_response_individuals")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FormResponseIndividual {

    @EmbeddedId
    private PKey id;

    @MapsId("questionResponseId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_response_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private QuestionResponse questionResponse;

    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class PKey {

        private UUID formResponseId;

        private Long questionResponseId;

    }
}
