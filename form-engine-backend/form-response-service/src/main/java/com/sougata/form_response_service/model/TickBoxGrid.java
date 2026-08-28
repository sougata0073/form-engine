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
@Table(name = "tick_box_grids")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TickBoxGrid extends AnyTypeQuestionResponse {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "tickBoxGrid")
    private List<TickBoxGridRow> responses = new ArrayList<>();

}
