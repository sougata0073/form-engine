package com.sougata.form_service.repository.formSchema;

import com.sougata.form_service.model.formSchema.Form;
import com.sougata.form_service.projection.FormSummaryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface FormRepository extends JpaRepository<Form, UUID> {

    List<FormSummaryProjection> findByUserIdOrderByLastOpenedOnDesc(UUID userId);

    @Modifying
    @Transactional
    @Query("update Form f set f.lastOpenedOn = :lastOpenedOn where f.id = :formId")
    void updateLastOpenedOn(UUID formId, Instant lastOpenedOn);

    @Modifying
    @Transactional
    @Query("update Form f set f.name = :newName where f.id = :formId")
    void renameForm(UUID formId, String newName);

}
