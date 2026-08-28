package com.sougata.form_data_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "file_uploads")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FileUpload extends AnyTypeQuestionResponse {

    private String fileName;

    @Column(columnDefinition = "text", nullable = false)
    private String fileUrl;

    @Column(nullable = false)
    private String fileMimeType;

    @Column(nullable = false)
    private Integer fileSize;

}
