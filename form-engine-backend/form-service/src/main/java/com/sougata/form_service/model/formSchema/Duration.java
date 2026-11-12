package com.sougata.form_service.model.formSchema;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "durations", schema = "form_schema")
@DynamicUpdate
public class Duration extends AnyTypeQuestion {
    @Override
    public Duration copy() {
        var copy = new Duration();

        copy.setQuestion(getQuestion());

        return copy;
    }
}
