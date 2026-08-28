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
@Table(name = "multiple_choice_templates", schema = "form_template")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MultipleChoiceTemplate extends AnyTypeQuestionTemplate {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "multipleChoiceTemplate")
    private List<MultipleChoiceOptionTemplate> options = new ArrayList<>();

}
