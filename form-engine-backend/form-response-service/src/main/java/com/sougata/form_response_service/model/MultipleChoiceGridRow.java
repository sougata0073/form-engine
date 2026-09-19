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
@Table(name = "multiple_choice_grid_rows")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MultipleChoiceGridRow {

    @Id
    @Tsid
    private Long id;

    @Column(nullable = false)
    private Long rowId;

    @Column(nullable = false)
    private Long responseColumnId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "multiple_choice_grid_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MultipleChoiceGrid multipleChoiceGrid;

}
