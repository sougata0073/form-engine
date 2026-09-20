package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.FormResponseSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface FormResponseRepository extends JpaRepository<FormResponseSummary, UUID> {

    @Modifying
    @Transactional
    @Query("update FormResponseSummary fr set fr.responseCount = fr.responseCount + :incrementBy where fr.formId = :formId")
    void incrementResponseCount(UUID formId, Long incrementBy);

    @Modifying
    @Transactional
    @Query("update FormResponseSummary fr set fr.responseCount = fr.responseCount - :decrementBy where fr.formId = :formId")
    void decrementResponseCount(UUID formId, Long decrementBy);
}
