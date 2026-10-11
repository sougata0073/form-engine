package com.sougata.form_engine.dto.others;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileTypeDetails {
    private String category;
    private List<String> mimeTypes;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FileTypeDetails that = (FileTypeDetails) o;

        var thisMimeTypesSet = new HashSet<>(mimeTypes);
        var thatMimeTypesSet = new HashSet<>(that.mimeTypes);

        if (!thisMimeTypesSet.equals(thatMimeTypesSet)) {
            return false;
        }

        return Objects.equals(category, that.category);
    }

}
