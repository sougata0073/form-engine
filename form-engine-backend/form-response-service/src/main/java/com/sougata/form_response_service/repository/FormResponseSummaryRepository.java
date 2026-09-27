package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.FormResponseSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface FormResponseSummaryRepository extends JpaRepository<FormResponseSummary, UUID> {

    @Modifying
    @Transactional
    @Query("update FormResponseSummary fr set fr.responseCount = fr.responseCount + :incrementBy where fr.formId = :formId")
    void incrementResponseCount(UUID formId, Long incrementBy);

    @Modifying
    @Transactional
    @Query("update FormResponseSummary fr set fr.responseCount = fr.responseCount - :decrementBy where fr.formId = :formId")
    void decrementResponseCount(UUID formId, Long decrementBy);

    @Query("select coalesce(sum(frs.responseCount), 0) from FormResponseSummary frs where frs.formId = :formId")
    Long findFormResponseCountByFormId(UUID formId);

    @Query(value = """
            select save_form_response_summary(
                :formId, :formResponseId, :incrementFormResponseCountBy, :incrementQuestionResponseCountBy,
                cast(:questionIdsJson as jsonb)
            )
            """, nativeQuery = true
    )
    void saveFormResponseSummary(
            UUID formId,
            UUID formResponseId,
            Long incrementFormResponseCountBy,
            Long incrementQuestionResponseCountBy,
            String questionIdsJson
    );

}
