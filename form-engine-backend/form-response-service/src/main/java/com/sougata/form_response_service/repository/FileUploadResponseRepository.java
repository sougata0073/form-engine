package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.FileUploadResponse;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("FILE_UPLOAD_RESPONSE_REPOSITORY")
public interface FileUploadResponseRepository extends AnyTypeQuestionResponseRepository<FileUploadResponse, Long> {

    @Query(value = """
            select file_upload_responses_increment_or_create(
                cast(:batchResponses as jsonb)
            )
            """, nativeQuery = true)
    void createOrIncrement(String batchResponses);

    @Query("""
            select
            fr.fileUrl fileUrl,
            fr.fileName fileName,
            fr.fileMimeType fileMimeType
            from FileUploadResponse fr
            where fr.questionId = :questionId
            order by fr.responseCount desc, fr.fileUrl, fr.fileName, fr.fileMimeType
            """)
    List<Tuple> getResponseFiles(Long questionId, Pageable pageable);

    @Query("""
            select
            f.fileUrl fileUrl,
            f.fileName fileName,
            f.fileMimeType fileMimeType,
            f.responseCount responseCount
            from FileUploadResponse f
            where f.questionId = :questionId
            order by f.responseCount desc, f.fileUrl, f.fileName, f.fileMimeType
            """)
    List<Tuple> groupedByFile(Long questionId, Pageable pageable);

    @Query(value = """
            select
            fri.form_response_id responseId,
            fri.user_id userId
            from file_upload_responses fur
            join question_response_form_response_individual qrfri
            on fur.question_response_id = qrfri.question_response_id
            and fur.question_id = :questionId
            and fur.file_url = :fUrl
            and fur.file_name = :fName
            and fur.file_mime_type = :fMimeType
            join form_response_individuals fri
            on fri.form_response_id = qrfri.form_response_individual_id
            order by fri.form_response_id
            """, nativeQuery = true)
    List<Tuple> getResponseIdsByGroupedResponse(Long questionId, String fName, String fUrl, String fMimeType, Pageable pageable);
}
