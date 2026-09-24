package com.sougata.form_data_service.model;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Table("form_responses")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FormResponse {

    @PrimaryKey
    private PartitionKey key;

    @Column("responded_question_ids")
    private Set<Long> respondedQuestionIds;

    @Column("submitted_on")
    private Instant submittedOn = Instant.now();

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @PrimaryKeyClass
    public static class PartitionKey {

        @PrimaryKeyColumn(name = "form_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private UUID formId;

        @PrimaryKeyColumn(name = "user_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
        private UUID userId;

        @PrimaryKeyColumn(name = "form_response_id", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
        @CassandraType(type = CassandraType.Name.TIMEUUID)
        private UUID formResponseId = Uuids.timeBased();

    }

}
