package com.sougata.form_response_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "file_upload_responses",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "file_upload_uk_file_url_file_name_file_mime_type_question_id",
                        columnNames = {
                                "file_url",
                                "file_name",
                                "file_mime_type",
                                "question_id"
                        }
                )
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadResponse extends AnyTypeQuestionResponse {

    @Column(nullable = false, columnDefinition = "text")
    private String fileUrl;

    @Column(nullable = false, columnDefinition = "text")
    private String fileName;

    @Column(nullable = false, columnDefinition = "text")
    private String fileMimeType;

}
