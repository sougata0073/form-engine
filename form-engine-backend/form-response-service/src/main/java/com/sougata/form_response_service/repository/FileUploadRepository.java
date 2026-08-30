package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.FileUpload;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("FILE_UPLOAD_RESPONSE_REPOSITORY")
public interface FileUploadRepository extends AnyTypeQuestionResponseRepository<FileUpload, Long> {

    @Query("""
            select
            f.fileName fileName,
            f.fileUrl fileUrl,
            f.fileMimeType fileMimeType
            from FileUpload f
            where f.questionResponse.questionId = :questionId
            group by f.fileName, f.fileUrl, f.fileMimeType
            order by count(f.questionResponseId) desc, f.fileName asc, f.fileUrl asc, f.fileMimeType asc
            """)
    List<Tuple> getResponseFiles(long questionId, Pageable pageable);

    @Query(value = """
            select
            f.file_name fileName,
            f.file_url fileUrl,
            f.file_mime_type fileMimeType,
            count(*) responseCount
            from form_responses fr
            left join question_responses qr
            on fr.id = qr.form_response_id
            and qr.question_id = :questionId
            left join file_uploads f
            on qr.id = f.question_response_id
            where fr.form_id = :formId
            group by f.file_name, f.file_url, f.file_mime_type
            order by responseCount desc, f.file_name asc, f.file_url asc, f.file_mime_type asc
            """, nativeQuery = true)
    List<Tuple> groupedByFile(UUID formId, long questionId, Pageable pageable);

    @Query("""
            select
            f.questionResponse.questionId questionId,
            f.fileName fileName,
            f.fileUrl fileUrl,
            f.fileMimeType fileMimeType
            from FileUpload f
            where f.questionResponse.formResponse.id = :formResponseId
            """)
    List<Tuple> getFileUploadsByFormResponse(long formResponseId);

    @Query(value = """
            select
            fr.id responseId,
            fr.user_id userId
            from form_responses fr
            left join question_responses qr
            on fr.id = qr.form_response_id
            and qr.question_id = :questionId
            left join file_uploads f
            on qr.id = f.question_response_id
            where fr.form_id = :formId and (
                (:fileName is null and f.file_name is null)
                or f.file_name = :fileName
            ) and (
                (:fileUrl is null and f.file_url is null)
                or f.file_url = :fileUrl
            ) and (
                (:fileMimeType is null and f.file_mime_type is null)
                or f.file_mime_type = :fileMimeType
            )
            order by fr.created_at, fr.id
            """, nativeQuery = true)
    List<Tuple> getResponseIdsByGroupedResponse(UUID formId, long questionId, String fileName, String fileUrl, String fileMimeType, Pageable pageable);

}
