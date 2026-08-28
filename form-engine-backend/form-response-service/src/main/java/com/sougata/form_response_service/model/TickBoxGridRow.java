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

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tick_box_grid_rows")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TickBoxGridRow implements Persistable<Long> {

    @Id
    private Long id = TsidCreator.getTsid().toLong();

    @Column(nullable = false)
    private Long rowId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "tick_box_grid_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TickBoxGrid tickBoxGrid;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "tickBoxGridRow")
    private List<TickBoxGridColumn> responses = new ArrayList<>();

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
