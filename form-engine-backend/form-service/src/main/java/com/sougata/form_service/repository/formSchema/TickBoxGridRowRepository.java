package com.sougata.form_service.repository.formSchema;

import com.sougata.form_service.model.formSchema.TickBoxGridRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TickBoxGridRowRepository extends JpaRepository<TickBoxGridRow, Long> {

    @Modifying
    @Transactional
    @Query("delete from TickBoxGridRow tbgr where tbgr.id = :rowId")
    void deleteRowById(Long rowId);

}
