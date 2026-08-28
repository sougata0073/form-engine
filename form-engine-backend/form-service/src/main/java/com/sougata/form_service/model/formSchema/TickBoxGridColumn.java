package com.sougata.form_service.model.formSchema;

import com.github.f4b6a3.tsid.TsidCreator;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "tick_box_grid_columns", schema = "form_schema")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TickBoxGridColumn implements Persistable<Long> {

    @Id
    private Long id = TsidCreator.getTsid().toLong();

    @Column(nullable = false)
    private String columnName;

    @Column(nullable = false)
    private Integer orderIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TickBoxGrid tickBoxGrid;

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
