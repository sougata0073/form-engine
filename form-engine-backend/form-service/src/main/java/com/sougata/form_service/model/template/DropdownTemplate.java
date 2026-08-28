package com.sougata.form_service.model.template;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dropdown_templates", schema = "form_template")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DropdownTemplate extends AnyTypeQuestionTemplate {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "dropdownTemplate")
    private List<DropdownOptionTemplate> options = new ArrayList<>();

}
