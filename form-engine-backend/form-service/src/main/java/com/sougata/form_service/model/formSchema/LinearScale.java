package com.sougata.form_service.model.formSchema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "linear_scales", schema = "form_schema")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldNameConstants
@DynamicUpdate
public class LinearScale extends AnyTypeQuestion {

    @Column(nullable = false)
    private Integer fromNumber;

    @Column(nullable = false)
    private Integer toNumber;

    @Override
    public LinearScale copy() {
        var copy = new LinearScale();

        copy.setQuestion(getQuestion());
        copy.setFromNumber(fromNumber);
        copy.setToNumber(toNumber);

        return copy;
    }
}
