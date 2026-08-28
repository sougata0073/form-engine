package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.DateTime;
import org.springframework.stereotype.Repository;

@Repository("DATE_TIME_RESPONSE_REPOSITORY")
public interface DateTimeRepository extends AnyTypeQuestionResponseRepository<DateTime, Long> {

}
