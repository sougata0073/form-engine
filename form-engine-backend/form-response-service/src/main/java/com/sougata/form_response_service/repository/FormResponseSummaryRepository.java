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

    @Query("select coalesce(sum(frs.responseCount), 0) from FormResponseSummary frs where frs.formId = :formId")
    Long findFormResponseCountByFormId(UUID formId);

    @Query(value = """
            select save_form_response_summaries(
                cast(:formResponseCountsJson as jsonb),
                cast(:questionResponseCountsJson as jsonb),
                cast(:formResponseIdsJson as jsonb)
            )
            """, nativeQuery = true
    )
    void saveFormResponseSummary(
            String formResponseCountsJson,
            String questionResponseCountsJson,
            String formResponseIdsJson
    );

    @Modifying
    @Transactional
    @Query("delete from FormResponseSummary frs where frs.formId = :formId")
    void deleteByFormId(UUID formId);

}
