package com.sougata.form_data_service.model;

import com.github.f4b6a3.tsid.TsidCreator;
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

    @Column("user_id")
    private UUID userId;

    @Column("responded_question_ids")
    private Set<Long> respondedQuestionIds;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @PrimaryKeyClass
    public static class PartitionKey {

        @PrimaryKeyColumn(name = "form_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private UUID formId;

        @PrimaryKeyColumn(name = "form_response_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
        private Long formResponseId = TsidCreator.getTsid().toLong();

        @PrimaryKeyColumn(name = "submitted_on", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
        private Instant submittedOn = Instant.now();

    }

}
