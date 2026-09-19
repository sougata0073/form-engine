package com.sougata.form_service.repository.formSchema;

import com.sougata.form_service.model.formSchema.MultipleChoiceOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MultipleChoiceOptionRepository extends JpaRepository<MultipleChoiceOption, Long> {

    @Modifying
    @Transactional
    @Query("delete from MultipleChoiceOption mco where mco.id = :optionId")
    void deleteOptionById(Long optionId);

}
