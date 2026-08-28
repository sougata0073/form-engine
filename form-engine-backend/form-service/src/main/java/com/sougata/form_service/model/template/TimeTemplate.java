package com.sougata.form_service.model.template;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "time_templates", schema = "form_template")
@EntityListeners(AuditingEntityListener.class)
public class TimeTemplate extends AnyTypeQuestionTemplate {
}
