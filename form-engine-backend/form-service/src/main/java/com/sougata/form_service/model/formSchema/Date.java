package com.sougata.form_service.model.formSchema;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "dates", schema = "form_schema")
@DynamicUpdate
public class Date extends AnyTypeQuestion {

    @Override
    public Date copy() {
        var copy = new Date();

        copy.setQuestion(getQuestion());

        return copy;
    }
}
