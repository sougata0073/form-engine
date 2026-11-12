package com.sougata.form_service.repository.formSchema;

import com.sougata.form_service.model.formSchema.FormGeneralSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface FormGeneralSettingsRepository extends JpaRepository<FormGeneralSettings, UUID> {

    @Modifying
    @Transactional
    @Query("""
                update FormGeneralSettings fgs
                set fgs.responderNeedsToBeAuthenticated = :needsToBeAuthenticated
                where fgs.formId = :formId
            """)
    void updateResponderNeedsToBeAuthenticated(UUID formId, Boolean needsToBeAuthenticated);

}
