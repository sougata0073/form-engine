package com.sougata.form_service.model.formSchema;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dropdowns", schema = "form_schema")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Dropdown extends AnyTypeQuestion {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "dropdown")
    private List<DropdownOption> options = new ArrayList<>();

}
