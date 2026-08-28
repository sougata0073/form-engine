package com.sougata.form_service.model.formSchema;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tick_box_grids", schema = "form_schema")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TickBoxGrid extends AnyTypeQuestion {

    @Column(nullable = false)
    private Boolean eachRowRequired;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "tickBoxGrid")
    private List<TickBoxGridRow> rows = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "tickBoxGrid")
    private List<TickBoxGridColumn> columns = new ArrayList<>();

}
