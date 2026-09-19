package com.sougata.form_service.repository.formSchema;

import com.sougata.form_service.model.formSchema.DropdownOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DropdownOptionRepository extends JpaRepository<DropdownOption, Long> {

    @Modifying
    @Transactional
    @Query("delete from DropdownOption do where do.id = :optionId")
    void deleteOptionById(Long optionId);

}
