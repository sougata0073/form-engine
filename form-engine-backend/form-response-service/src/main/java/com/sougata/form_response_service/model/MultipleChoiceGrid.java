package com.sougata.form_response_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "multiple_choice_grids")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MultipleChoiceGrid extends AnyTypeQuestionResponse {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "multipleChoiceGrid")
    private List<MultipleChoiceGridRow> responses = new ArrayList<>();

}
