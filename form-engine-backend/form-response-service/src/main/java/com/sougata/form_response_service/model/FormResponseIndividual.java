package com.sougata.form_response_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Persistable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "form_response_individuals",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_form_id_user_id",
                        columnNames = {
                                "form_response_summary_form_id",
                                "user_id"
                        }
                )
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FormResponseIndividual implements Persistable<UUID> {

    @Id
    private UUID formResponseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private FormResponseSummary formResponseSummary;

    @Column(nullable = false)
    private UUID userId;

    @ManyToMany(mappedBy = "formResponseIndividuals")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<QuestionResponse> questionResponses = new ArrayList<>();

    @Transient
    private boolean isNew;

    @Override
    public @Nullable UUID getId() {
        return formResponseId;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostPersist
    @PostLoad
    void markNotNew() {
        isNew = false;
    }
}
