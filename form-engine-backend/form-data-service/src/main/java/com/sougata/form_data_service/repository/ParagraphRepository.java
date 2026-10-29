package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.Paragraph;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository("PARAGRAPH_RESPONSE_REPOSITORY")
public interface ParagraphRepository extends AnyTypeQuestionResponseRepository<Paragraph, AnyTypeQuestionResponse.PartitionKey> {

    @Query("delete from paragraphs where form_id = :formId and form_response_id = :formResponseId and question_id in :questionIds")
    void deleteAllByQuestionIds(UUID formId, UUID formResponseId, Set<Long> questionIds);

    @Query("delete from paragraphs where form_id = :formId")
    void deleteAllByFormId(UUID formId);

    @Query("select * from paragraphs where form_id = :formId and form_response_id = :formResponseId and question_id in :questionIds")
    List<Paragraph> findAllByQuestionIds(UUID formId, UUID formResponseId, Set<Long> questionIds);
}
