package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.FormResponse;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormResponseRepository extends CassandraRepository<FormResponse, FormResponse.PartitionKey> {

}
