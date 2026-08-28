package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.Date;
import org.springframework.stereotype.Repository;

@Repository("DATE_RESPONSE_REPOSITORY")
public interface DateRepository extends AnyTypeQuestionResponseRepository<Date, Long> {
}
