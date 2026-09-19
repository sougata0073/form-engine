package com.sougata.form_response_service.model;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "tick_box_grid_columns")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TickBoxGridColumn {

    @Id
    @Tsid
    private Long id;

    @Column(nullable = false)
    private Long responseOptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "tick_box_grid_row_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TickBoxGridRow tickBoxGridRow;

}
