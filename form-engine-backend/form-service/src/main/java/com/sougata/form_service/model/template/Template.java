package com.sougata.form_service.model.template;

import com.github.f4b6a3.tsid.TsidCreator;
import com.sougata.form_service.model.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "templates", schema = "form_template")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Template extends Auditable implements Persistable<Long> {

    @Id
    private Long id = TsidCreator.getTsid().toLong();

    @Column(columnDefinition = "text")
    private String name;

    @Column(columnDefinition = "text")
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TemplateCategory category;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "template")
    private List<QuestionTemplate> questionTemplates = new ArrayList<>();

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
