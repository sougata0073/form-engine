package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.FormResponse;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FormResponseRepository extends CassandraRepository<FormResponse, FormResponse.PartitionKey> {

    @Query("delete from form_responses where form_id = :formId")
    void deleteAllByFormId(UUID formId);

}
