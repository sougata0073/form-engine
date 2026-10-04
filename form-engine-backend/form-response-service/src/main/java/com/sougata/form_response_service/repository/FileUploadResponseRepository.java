package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.FileUploadResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("FILE_UPLOAD_RESPONSE_REPOSITORY")
public interface FileUploadResponseRepository extends AnyTypeQuestionResponseRepository<FileUploadResponse, Long> {

    @Query(value = """
            select file_upload_responses_increment_or_create(
                cast(:batchResponses as jsonb)
            )
            """, nativeQuery = true)
    void createOrIncrement(String batchResponses);

}
