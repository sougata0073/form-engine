package com.sougata.form_response_service.model;

import com.github.f4b6a3.tsid.TsidCreator;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "form_responses",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_form_id_user_id",
                        columnNames = {"form_id", "user_id"}
                )
        }
)
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FormResponse extends Auditable implements Persistable<Long> {

    @Id
    private Long id = TsidCreator.getTsid().toLong();

    @Column(nullable = false)
    private UUID formId;

    @Column(nullable = false)
    private UUID userId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "formResponse")
    private List<QuestionResponse> questionResponses = new ArrayList<>();

    @Transient
    private boolean isNew = true;

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
