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
}
