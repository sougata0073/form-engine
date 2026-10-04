package com.sougata.form_response_service.model;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "question_responses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResponse {

    @Id
    @Tsid
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private QuestionResponseSummary questionResponseSummary;

    @ManyToMany
    @JoinTable(
            name = "question_response_form_response_individual",
            joinColumns = @JoinColumn(
                    name = "question_response_id", nullable = false
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "form_response_individual_id", nullable = false
            ),
            uniqueConstraints = {
                    @UniqueConstraint(
                            name = "unique_question_response_id_form_response_individual_id",
                            columnNames = {"question_response_id", "form_response_individual_id"}
                    )
            }
    )
    private List<FormResponseIndividual> formResponseIndividuals = new ArrayList<>();

}
