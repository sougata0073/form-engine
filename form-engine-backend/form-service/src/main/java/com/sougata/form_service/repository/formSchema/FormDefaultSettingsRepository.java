package com.sougata.form_service.repository.formSchema;

import com.sougata.form_service.model.formSchema.FormDefaultSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FormDefaultSettingsRepository extends JpaRepository<FormDefaultSettings, UUID> {
}
