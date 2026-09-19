package com.sougata.form_service.repository.formSchema;

import com.sougata.form_service.model.formSchema.MultipleChoiceGridColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MultipleChoiceGridColumnRepository extends JpaRepository<MultipleChoiceGridColumn, Long> {

    @Modifying
    @Transactional
    @Query("delete from MultipleChoiceGridColumn mcgc where mcgc.id = :columnId")
    void deleteColumnById(Long columnId);

}
