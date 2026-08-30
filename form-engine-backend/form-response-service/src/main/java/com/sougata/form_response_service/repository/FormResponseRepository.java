package com.sougata.form_response_service.repository;

import com.sougata.form_engine.constant.cache.FormResponseCacheNames;
import com.sougata.form_response_service.model.FormResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FormResponseRepository extends JpaRepository<FormResponse, Long> {

    Optional<FormResponse> findByFormIdAndId(UUID formId, Long id);

    @Query("""
            select
            count(fr.id)
            from FormResponse fr
            where fr.formId = :formId
            """)
    Long getFormResponseCount(UUID formId);

    boolean existsByFormIdAndUserId(UUID formId, UUID userId);

    @Query("""
            select
            x.rn - 1
            from (
                select
                fr.id formResponseId,
                row_number() over (order by fr.createdAt, fr.id) rn
                from FormResponse fr
                where fr.formId = :formId
            ) x
            where x.formResponseId = :formResponseId
            """)
    Optional<Long> getPageNumberOfFormResponse(UUID formId, long formResponseId);

    @Cacheable(
            cacheNames = {FormResponseCacheNames.FORM_RESPONSE_ID},
            key = "'formId=' + #formId + '::page=' + #page"
    )
    @Query("""
            select
            x.formResponseId
            from (
                select
                fr.id formResponseId,
                row_number() over (order by fr.createdAt, fr.id) rn
                from FormResponse fr
                where fr.formId = :formId
            ) x
            where (x.rn - 1) = :page
            """)
    Optional<Long> getFormResponseIdFromPage(UUID formId, long page);
}
