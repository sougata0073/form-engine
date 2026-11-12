package com.sougata.form_service.model.formSchema;

import com.sougata.form_engine.common.Copyable;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "tick_box_grid_columns", schema = "form_schema")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldNameConstants
@DynamicUpdate
public class TickBoxGridColumn implements Copyable<TickBoxGridColumn> {

    @Id
    @Tsid
    private Long id;

    @Column(nullable = false)
    private String columnName;

    @Column(nullable = false)
    private Integer orderIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TickBoxGrid tickBoxGrid;

    @Override
    public TickBoxGridColumn copy() {
        var copy = new TickBoxGridColumn();

        copy.setColumnName(columnName);
        copy.setOrderIndex(orderIndex);
        copy.setTickBoxGrid(tickBoxGrid);

        return copy;
    }
}
