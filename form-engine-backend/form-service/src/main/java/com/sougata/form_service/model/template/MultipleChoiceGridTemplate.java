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
@Table(name = "multiple_choice_grid_templates", schema = "form_template")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MultipleChoiceGridTemplate extends AnyTypeQuestionTemplate {

    @Column(nullable = false)
    private Boolean eachRowRequired;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "multipleChoiceGridTemplate")
    private List<MultipleChoiceGridRowTemplate> rows = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "multipleChoiceGridTemplate")
    private List<MultipleChoiceGridColumnTemplate> columns = new ArrayList<>();
}
