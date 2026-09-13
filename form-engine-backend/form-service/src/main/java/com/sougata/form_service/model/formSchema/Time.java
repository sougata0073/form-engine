package com.sougata.form_service.model.formSchema;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "times", schema = "form_schema")
@FieldNameConstants
@DynamicUpdate
public class Time extends AnyTypeQuestion {
}
