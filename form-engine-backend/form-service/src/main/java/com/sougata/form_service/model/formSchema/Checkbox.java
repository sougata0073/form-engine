package com.sougata.form_service.model.formSchema;

import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "checkboxes", schema = "form_schema")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Checkbox extends AnyTypeQuestion {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "checkbox")
    private List<CheckboxOption> options = new ArrayList<>();

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private JsonNode validationConfig;
}
