package com.sougata.form_service.model;

import com.sougata.form_engine.common.Copyable;
import io.hypersistence.utils.hibernate.type.array.StringArrayType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "file_types", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FileType implements Copyable<FileType> {

    @Id
    private String category;

    @Type(StringArrayType.class)
    @Column(nullable = false, columnDefinition = "text[]")
    private String[] mimeTypes;

    @Override
    public FileType copy() {
        var copy = new FileType();

        copy.setCategory(category);
        copy.setMimeTypes(mimeTypes);

        return copy;
    }
}
