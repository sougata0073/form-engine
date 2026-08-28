package com.sougata.form_service.model.template;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "linear_scale_templates", schema = "form_template")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LinearScaleTemplate extends AnyTypeQuestionTemplate {

    @Column(nullable = false)
    private Integer fromNumber;

    @Column(nullable = false)
    private Integer toNumber;
}
