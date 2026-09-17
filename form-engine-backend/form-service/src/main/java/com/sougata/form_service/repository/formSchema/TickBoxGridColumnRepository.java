package com.sougata.form_service.repository.formSchema;

import com.sougata.form_service.model.formSchema.TickBoxGridColumn;
import com.sougata.form_service.model.formSchema.TickBoxGridRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TickBoxGridColumnRepository extends JpaRepository<TickBoxGridColumn, Long> {
}
