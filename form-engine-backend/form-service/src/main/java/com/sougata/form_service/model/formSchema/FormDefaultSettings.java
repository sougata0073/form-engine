package com.sougata.form_service.model.formSchema;

import com.sougata.form_engine.constant.FormEmailCollectionOption;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Persistable;

import java.util.UUID;

@Entity
@Table(name = "form_default_settings", schema = "form_schema")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DynamicUpdate
public class FormDefaultSettings implements Persistable<UUID> {

    @Id
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FormEmailCollectionOption defaultEmailCollectionOption = FormEmailCollectionOption.OFF;

    @Column(nullable = false)
    private Boolean makeQuestionsRequiredByDefault = false;

    @Transient
    private boolean isNew = true;

    @Override
    public @Nullable UUID getId() {
        return userId;
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
