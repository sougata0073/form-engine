package com.sougata.form_service.model.formSchema;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "durations", schema = "form_schema")
public class Duration extends AnyTypeQuestion {
}
