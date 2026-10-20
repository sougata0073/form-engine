package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.FormResponseUser;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormResponseUserRepository extends CassandraRepository<FormResponseUser, FormResponseUser.PartitionKey> {
}
