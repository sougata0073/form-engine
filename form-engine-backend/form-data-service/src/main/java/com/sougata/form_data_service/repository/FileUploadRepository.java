package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.FileUpload;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("FILE_UPLOAD_RESPONSE_REPOSITORY")
public interface FileUploadRepository extends AnyTypeQuestionResponseRepository<FileUpload, AnyTypeQuestionResponse.PartitionKey> {

    @Query("delete from file_uploads where question_id = :questionId")
    void deleteAllByQuestionId(Long questionId);

    @Query("""
            delete
            from file_uploads
            where question_id = :questionId
            and question_response_id = :questionResponseId
            """)
    void deleteAllByQuestionIdAndQuestionResponseId(Long questionId, Long questionResponseId);
}
