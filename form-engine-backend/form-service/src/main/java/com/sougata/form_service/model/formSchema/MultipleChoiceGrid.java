package com.sougata.form_service.model.formSchema;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "multiple_choice_grids", schema = "form_schema")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldNameConstants
@DynamicUpdate
public class MultipleChoiceGrid extends AnyTypeQuestion {

    @Column(nullable = false)
    private Boolean eachRowRequired;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "multipleChoiceGrid")
    private List<MultipleChoiceGridRow> rows = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "multipleChoiceGrid")
    private List<MultipleChoiceGridColumn> columns = new ArrayList<>();

    @Override
    public MultipleChoiceGrid copy() {
        var copy = new MultipleChoiceGrid();

        copy.setQuestion(getQuestion());
        copy.setEachRowRequired(eachRowRequired);

        var newRows = rows.stream().map(row -> {
            var rowCopy = row.copy();
            rowCopy.setMultipleChoiceGrid(copy);

            return rowCopy;
        }).toList();

        var newColumns = columns.stream().map(column -> {
            var columnCopy = column.copy();
            columnCopy.setMultipleChoiceGrid(copy);

            return columnCopy;
        }).toList();

        copy.setRows(newRows);
        copy.setColumns(newColumns);

        return copy;
    }

}
