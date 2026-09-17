package com.sougata.form_service.repository.formSchema;

import com.sougata.form_service.model.formSchema.MultipleChoiceGridRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MultipleChoiceGridRowRepository extends JpaRepository<MultipleChoiceGridRow, Long> {
}
