package com.sougata.form_service.repository.formSchema;

import com.sougata.form_service.model.formSchema.CheckboxOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CheckboxOptionRepository extends JpaRepository<CheckboxOption, Long> {

    @Modifying
    @Transactional
    @Query("delete from CheckboxOption co where co.id = :optionId")
    void deleteOptionById(Long optionId);

}
