package com.sougata.form_service.model.formSchema;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "dates", schema = "form_schema")
@FieldNameConstants
@DynamicUpdate
public class Date extends AnyTypeQuestion {

}
