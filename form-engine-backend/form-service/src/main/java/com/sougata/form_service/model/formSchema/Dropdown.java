package com.sougata.form_service.model.formSchema;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dropdowns", schema = "form_schema")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DynamicUpdate
public class Dropdown extends AnyTypeQuestion {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "dropdown")
    private List<DropdownOption> options = new ArrayList<>();

    @Override
    public Dropdown copy() {
        var copy = new Dropdown();

        copy.setQuestion(getQuestion());

        var newOptions = options.stream().map(op -> {
            var opCopy = op.copy();
            opCopy.setDropdown(copy);

            return opCopy;
        }).toList();

        copy.setOptions(newOptions);

        return copy;
    }

}
