package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.FormResponseIndividual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FormResponseIndividualRepository extends JpaRepository<FormResponseIndividual, UUID> {

    @Query(value = """
            select
            x.rn - 1
            from (
              select
              fri.form_response_id,
              row_number() over (order by fri.form_response_id) rn
              from form_response_individuals fri
              where fri.form_response_summary_form_id = :formId
            ) x
            where x.form_response_id = :formResponseId
            """, nativeQuery = true)
    Long getFormResponsePageByFormResponseId(UUID formId, UUID formResponseId);

    @Query(value = """
            select
            x.form_response_id
            from (
              select
              fri.form_response_id,
              row_number() over (order by fri.form_response_id) rn
              from form_response_individuals fri
              where fri.form_response_summary_form_id = :formId
            ) x
            where (x.rn - 1) = :formResponsePage
            """, nativeQuery = true)
    UUID getFormResponseIdByFormResponsePage(UUID formId, Long formResponsePage);

    @Query(value = """
            select
            exists (
              select 1
              from form_response_individuals fri
              where fri.form_response_summary_form_id = :formId
              and fri.user_id = :userId
            )
            """, nativeQuery = true)
    boolean existsByFormIdAndUserId(UUID formId, UUID userId);

}
