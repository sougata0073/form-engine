package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.FormResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    @Modifying
    @Transactional
    @Query("delete from FormResponse fr where fr.id = :formResponseId")
    void deleteByFormResponseId(Long formResponseId);

}
