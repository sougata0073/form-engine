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

@Entity
@Table(name = "question_response_summaries")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResponseSummary implements Persistable<Long> {

    @Id
    private Long questionId;

    @Column(nullable = false)
    private Long responseCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private FormResponseSummary formResponseSummary;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "questionResponseSummary")
    private List<QuestionResponse> questionResponses = new ArrayList<>();

    @Transient
    private boolean isNew = true;

    @Override
    public @Nullable Long getId() {
        return questionId;
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
