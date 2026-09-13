package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AnyTypeQuestionResponseRepository<Q extends AnyTypeQuestionResponse, ID> extends CassandraRepository<Q, ID> {
}
