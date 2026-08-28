package com.sougata.form_service.model.template;

import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "checkbox_templates", schema = "form_template")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CheckboxTemplate extends AnyTypeQuestionTemplate {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "checkboxTemplate")
    private List<CheckboxOptionTemplate> options = new ArrayList<>();

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private JsonNode validationConfig;
}
