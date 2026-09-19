package com.sougata.form_service.repository.formSchema;

import com.sougata.form_service.model.formSchema.MultipleChoiceGridRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MultipleChoiceGridRowRepository extends JpaRepository<MultipleChoiceGridRow, Long> {

    @Modifying
    @Transactional
    @Query("delete from MultipleChoiceGridRow mcgr where mcgr.id = :rowId")
    void deleteRowById(Long rowId);

}
