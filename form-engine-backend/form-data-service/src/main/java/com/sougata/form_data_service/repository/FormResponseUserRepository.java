package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.FormResponseUser;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FormResponseUserRepository extends CassandraRepository<FormResponseUser, FormResponseUser.PartitionKey> {

    @Query("delete from form_responses_user where form_id = :formId")
    void deleteAllByFormId(UUID formId);

}
