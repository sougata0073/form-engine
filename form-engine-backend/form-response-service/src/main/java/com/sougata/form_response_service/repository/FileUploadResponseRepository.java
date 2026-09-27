package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.FileUploadResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("FILE_UPLOAD_RESPONSE_REPOSITORY")
public interface FileUploadResponseRepository extends AnyTypeQuestionResponseRepository<FileUploadResponse, Long> {

    @Query(value = """
            select file_upload_responses_increment_or_create(
                cast(:data as jsonb), :formResponseId, :incrementBy
            )
            """, nativeQuery = true)
    void createOrIncrement(String data, UUID formResponseId, Long incrementBy);

}
