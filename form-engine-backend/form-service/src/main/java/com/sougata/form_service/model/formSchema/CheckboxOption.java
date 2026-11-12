package com.sougata.form_service.model.formSchema;

import com.sougata.form_engine.common.Copyable;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "checkbox_options", schema = "form_schema")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DynamicUpdate
public class CheckboxOption implements Copyable<CheckboxOption> {

    @Id
    @Tsid
    private Long id;

    @Column(nullable = false)
    private String option;

    @Column(nullable = false)
    private Integer orderIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Checkbox checkbox;

    @Override
    public CheckboxOption copy() {
        var copy = new CheckboxOption();

        copy.setOption(option);
        copy.setOrderIndex(orderIndex);
        copy.setCheckbox(checkbox);

        return copy;
    }
}
