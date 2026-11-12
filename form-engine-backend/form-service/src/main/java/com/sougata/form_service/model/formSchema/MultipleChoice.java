package com.sougata.form_service.model.formSchema;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "multiple_choices", schema = "form_schema")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldNameConstants
@DynamicUpdate
public class MultipleChoice extends AnyTypeQuestion {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "multipleChoice")
    private List<MultipleChoiceOption> options = new ArrayList<>();

    @Override
    public MultipleChoice copy() {
        var copy = new MultipleChoice();

        copy.setQuestion(getQuestion());

        var newOptions = options.stream().map(op -> {
            var opCopy = op.copy();
            opCopy.setMultipleChoice(copy);

            return opCopy;
        }).toList();

        copy.setOptions(newOptions);

        return copy;
    }

}
