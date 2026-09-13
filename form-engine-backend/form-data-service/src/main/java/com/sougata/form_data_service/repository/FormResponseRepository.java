package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.FormResponse;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FormResponseRepository extends CassandraRepository<FormResponse, FormResponse.PartitionKey> {

    @Query("""
            select
            count(fr.id)
            from FormResponse fr
            where fr.formId = :formId
            """)
    Long getFormResponseCount(UUID formId);

    @Query("delete from FormResponse fr where fr.id = :formResponseId")
    void deleteByFormResponseId(Long formResponseId);

}
