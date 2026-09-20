package com.sougata.form_response_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Persistable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "form_responses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FormResponseSummary implements Persistable<UUID> {

    @Id
    private UUID formId;

    @Column(nullable = false)
    private Long responseCount;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "formResponseSummary")
    private List<QuestionResponseSummary> questionResponseSummaries = new ArrayList<>();

    @Transient
    private boolean isNew = true;

    @Override
    public @Nullable UUID getId() {
        return formId;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostLoad
    @PostPersist
    void markNotNew() {
        isNew = false;
    }
}
