package com.sougata.form_service.model.formSchema;

import com.sougata.form_service.model.FileType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "file_uploads", schema = "form_schema")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DynamicUpdate
public class FileUpload extends AnyTypeQuestion {

    @ManyToMany
    @JoinTable(
            name = "file_upload_file_type",
            schema = "form_schema",
            joinColumns = @JoinColumn(
                    name = "file_upload_id", nullable = false
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "file_type_id", nullable = false
            ),
            uniqueConstraints = {
                    @UniqueConstraint(
                            name = "unique_file_upload_id_file_type_id",
                            columnNames = {"file_upload_id", "file_type_id"}
                    )
            }
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<FileType> allowedFileTypes;

    @Column(nullable = false)
    private Integer maxFileSize;

    @Override
    public FileUpload copy() {
        var copy = new FileUpload();

        copy.setQuestion(getQuestion());
        copy.setMaxFileSize(maxFileSize);

        var newAllowedFileTypes = allowedFileTypes.stream().map(FileType::copy).toList();

        copy.setAllowedFileTypes(newAllowedFileTypes);

        return copy;
    }
}
