package com.sougata.form_service.model.template;

import com.sougata.form_service.model.FileType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Entity
@Table(name = "file_upload_templates", schema = "form_template")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FileUploadTemplate extends AnyTypeQuestionTemplate {

    @ManyToMany
    @JoinTable(
            name = "file_upload_template_file_type",
            schema = "form_template",
            joinColumns = @JoinColumn(
                    name = "file_upload_template_id", nullable = false
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "file_type_id", nullable = false
            ),
            uniqueConstraints = {
                    @UniqueConstraint(
                            name = "unique_file_upload_template_id_file_type_id",
                            columnNames = {"file_upload_template_id", "file_type_id"}
                    )
            }
    )
    private List<FileType> allowedFileTypeTemplates;

    @Column(nullable = false)
    private Integer maxFileSize;
}
