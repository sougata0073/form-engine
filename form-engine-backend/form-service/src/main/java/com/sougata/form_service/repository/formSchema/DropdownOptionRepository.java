package com.sougata.form_service.repository.formSchema;

import com.sougata.form_service.model.formSchema.DropdownOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DropdownOptionRepository extends JpaRepository<DropdownOption, Long> {
}
