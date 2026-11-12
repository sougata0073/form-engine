package com.sougata.form_service.model.formSchema;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "times", schema = "form_schema")
@DynamicUpdate
public class Time extends AnyTypeQuestion {
    @Override
    public Time copy() {
        var copy = new Time();

        copy.setQuestion(getQuestion());

        return copy;
    }
}
