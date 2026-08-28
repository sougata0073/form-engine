package com.sougata.form_response_service.model;

import com.github.f4b6a3.tsid.TsidCreator;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "tick_box_grid_columns")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TickBoxGridColumn implements Persistable<Long> {

    @Id
    private Long id = TsidCreator.getTsid().toLong();

    @Column(nullable = false)
    private Long responseOptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "tick_box_grid_row_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TickBoxGridRow tickBoxGridRow;

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
