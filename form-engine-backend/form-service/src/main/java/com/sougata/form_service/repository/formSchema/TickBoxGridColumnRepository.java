package com.sougata.form_service.repository.formSchema;

import com.sougata.form_service.model.formSchema.TickBoxGridColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TickBoxGridColumnRepository extends JpaRepository<TickBoxGridColumn, Long> {

    @Modifying
    @Transactional
    @Query("delete from TickBoxGridColumn tbgc where tbgc.id = :columnId")
    void deleteColumnById(Long columnId);

}
