package com.sougata.form_service.repository.formSchema;

import com.sougata.form_service.model.formSchema.MultipleChoiceOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MultipleChoiceOptionRepository extends JpaRepository<MultipleChoiceOption, Long> {
}
