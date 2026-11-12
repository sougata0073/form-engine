package com.sougata.form_service.model.formSchema;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "date_times", schema = "form_schema")
@DynamicUpdate
public class DateTime extends AnyTypeQuestion {
    @Override
    public DateTime copy() {
        var copy = new DateTime();

        copy.setQuestion(getQuestion());

        return copy;
    }
}
